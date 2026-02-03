package com.cliptozip.authsvc.application.usecase;

import com.cliptozip.authsvc.application.exception.InvalidTokenException;
import com.cliptozip.authsvc.application.port.out.TokenCachePort;
import com.cliptozip.authsvc.application.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateTokenUseCaseTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenCachePort tokenCachePort;

    @InjectMocks
    private ValidateTokenUseCase validateTokenUseCase;

    private final String validToken = "valid-jwt-token";
    private final String userEmail = "test@example.com";

    @Test
    void whenTokenIsValidAndInCache_shouldReturnUserEmail() {
        // Given
        when(jwtService.extractClaim(eq(validToken), any())).thenReturn(userEmail);
        when(tokenCachePort.getToken(userEmail)).thenReturn(validToken);
        when(jwtService.isTokenValid(validToken, userEmail)).thenReturn(true);

        // When
        String resultEmail = validateTokenUseCase.validate(validToken);

        // Then
        assertThat(resultEmail).isEqualTo(userEmail);
    }

    @Test
    void whenTokenIsNull_shouldThrowInvalidTokenException() {
        // When & Then
        InvalidTokenException ex = assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(null));
        assertThat(ex.getMessage()).isEqualTo("Token is missing or empty.");
    }

    @Test
    void whenTokenIsEmpty_shouldThrowInvalidTokenException() {
        // When & Then
        InvalidTokenException ex = assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(""));
        assertThat(ex.getMessage()).isEqualTo("Token is missing or empty.");
    }

    @Test
    void whenTokenIsNotInCache_shouldThrowInvalidTokenException() {
        // Given
        when(jwtService.extractClaim(eq(validToken), any())).thenReturn(userEmail);
        when(tokenCachePort.getToken(userEmail)).thenReturn(null);

        // When & Then
        assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(validToken));
    }

    @Test
    void whenTokenDoesNotMatchCachedToken_shouldThrowInvalidTokenException() {
        // Given
        String differentToken = "different-valid-token";
        when(jwtService.extractClaim(eq(validToken), any())).thenReturn(userEmail);
        when(tokenCachePort.getToken(userEmail)).thenReturn(differentToken);

        // When & Then
        assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(validToken));
    }

    @Test
    void whenTokenIsExpiredOrInvalidSignature_shouldThrowInvalidTokenException() {
        // Given
        when(jwtService.extractClaim(eq(validToken), any())).thenReturn(userEmail);
        when(tokenCachePort.getToken(userEmail)).thenReturn(validToken);
        when(jwtService.isTokenValid(validToken, userEmail)).thenReturn(false);

        // When & Then
        assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(validToken));
    }

    @Test
    void whenTokenIsMalformed_shouldThrowInvalidTokenException() {
        // Given
        String malformedToken = "this-is-not-a-jwt";
        when(jwtService.extractClaim(eq(malformedToken), any())).thenThrow(new RuntimeException("JWT parsing error"));

        // When & Then
        assertThrows(InvalidTokenException.class, () -> validateTokenUseCase.validate(malformedToken));
    }
}
