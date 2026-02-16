package com.cliptozip.authsvc.application.usecase;

import com.cliptozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.cliptozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.cliptozip.authsvc.application.exception.InvalidCredentialsException;
import com.cliptozip.authsvc.application.port.out.TokenCachePort;
import com.cliptozip.authsvc.application.port.out.UserPersistencePort;
import com.cliptozip.authsvc.application.service.JwtService;
import com.cliptozip.authsvc.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenCachePort tokenCachePort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("test@example.com", "password123");
        user = new User("user-id", "Test User", "test@example.com", "encodedPassword");
    }

    @Test
    void whenNoTokenInCache_shouldGenerateNewTokenAndCacheIt() {
        // Given
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.of(user));
        when(tokenCachePort.getToken(user.getUserId())).thenReturn(null); // No token in cache
        when(jwtService.generateToken(user)).thenReturn("new-fake-jwt-token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        TokenResponse tokenResponse = authUseCase.login(loginRequest);

        // Then
        assertThat(tokenResponse.token()).isEqualTo("new-fake-jwt-token");
        verify(tokenCachePort).cacheToken(user.getUserId(), "new-fake-jwt-token", 3600L, TimeUnit.SECONDS);
    }

    @Test
    void whenValidTokenExistsInCache_shouldReturnCachedToken() {
        // Given
        String cachedToken = "valid-cached-token";
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.of(user));
        when(tokenCachePort.getToken(user.getUserId())).thenReturn(cachedToken);
        when(jwtService.isTokenValid(cachedToken, user.getUserId())).thenReturn(true);
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        TokenResponse tokenResponse = authUseCase.login(loginRequest);

        // Then
        assertThat(tokenResponse.token()).isEqualTo(cachedToken);
        verify(jwtService, never()).generateToken(any(User.class)); // Ensure new token is not generated
        verify(tokenCachePort, never()).cacheToken(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void whenInvalidTokenExistsInCache_shouldGenerateNewToken() {
        // Given
        String expiredToken = "expired-cached-token";
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.of(user));
        when(tokenCachePort.getToken(user.getUserId())).thenReturn(expiredToken);
        when(jwtService.isTokenValid(expiredToken, user.getUserId())).thenReturn(false); // Token is invalid
        when(jwtService.generateToken(user)).thenReturn("new-fresh-token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        TokenResponse tokenResponse = authUseCase.login(loginRequest);

        // Then
        assertThat(tokenResponse.token()).isEqualTo("new-fresh-token");
        verify(tokenCachePort).deleteToken(user.getUserId()); // Verify old token is deleted
        verify(tokenCachePort).cacheToken(user.getUserId(), "new-fresh-token", 3600L, TimeUnit.SECONDS); // Verify new token is cached
    }

    @Test
    void whenInvalidCredentials_shouldThrowInvalidCredentialsException() {
        // Given
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> {
            authUseCase.login(loginRequest);
        });
        verify(tokenCachePort, never()).getToken(anyString()); // Ensure cache is not checked on auth failure
    }

    @Test
    void whenUnexpectedErrorOccurs_shouldThrowRuntimeException() {
        // Given
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.of(user));
        when(tokenCachePort.getToken(user.getUserId())).thenThrow(new RuntimeException("Cache unavailable"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authUseCase.login(loginRequest);
        });
    }
}
