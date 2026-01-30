package com.clicktozip.authsvc.application.usecase;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.application.service.JwtService;
import com.clicktozip.authsvc.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @Mock
    private JwtService jwtService;

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
    void whenLoginSuccessful_thenReturnTokenResponse() {
        // Given
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        TokenResponse tokenResponse = authUseCase.login(loginRequest);

        // Then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.token()).isEqualTo("fake-jwt-token");
        assertThat(tokenResponse.tokenType()).isEqualTo("Bearer");
        assertThat(tokenResponse.expiresInSeconds()).isEqualTo(3600L);
    }

    @Test
    void whenUserNotFound_thenThrowRuntimeException() {
        // Given
        when(userPersistencePort.findByEmailAndPassword(loginRequest.email(), loginRequest.password())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authUseCase.login(loginRequest);
        });
        assertThat(exception.getMessage()).isEqualTo("Invalid email or password");
    }
}
