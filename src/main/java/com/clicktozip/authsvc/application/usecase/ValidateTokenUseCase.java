package com.clicktozip.authsvc.application.usecase;

import com.clicktozip.authsvc.application.port.in.ValidateTokenUseCasePort;
import com.clicktozip.authsvc.application.port.out.TokenCachePort;
import com.clicktozip.authsvc.application.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateTokenUseCase implements ValidateTokenUseCasePort {

    private final JwtService jwtService;
    private final TokenCachePort tokenCachePort;

    @Override
    public boolean validate(String token) {
        if (token == null || token.isEmpty()) {
            log.warn("Validation failed: Token is missing or empty.");
            return false;
        }

        try {
            // 1. Extract email from the token
            final String userEmail = jwtService.extractClaim(token, Claims::getSubject);

            // 2. Check if the token exists in the cache
            String cachedToken = tokenCachePort.getToken(userEmail);
            if (cachedToken == null) {
                log.warn("Validation failed for user {}: Token not found in cache.", userEmail);
                return false;
            }

            // 3. Check if the provided token matches the one in the cache
            if (!cachedToken.equals(token)) {
                log.warn("Validation failed for user {}: Provided token does not match cached token.", userEmail);
                return false;
            }

            // 4. Use JwtService to validate expiration and signature
            if (!jwtService.isTokenValid(token, userEmail)) {
                log.warn("Validation failed for user {}: Token is expired or has invalid signature.", userEmail);
                return false;
            }

            // If all checks pass, the token is valid
            log.info("Token successfully validated for user: {}", userEmail);
            return true;

        } catch (Exception e) {
            // Catches any parsing errors from jwtService.extractClaim or other unexpected issues
            log.error("An exception occurred during token validation: {}", e.getMessage());
            return false;
        }
    }
}
