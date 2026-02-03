package com.cliptozip.authsvc.application.usecase;

import com.cliptozip.authsvc.application.exception.InvalidTokenException;
import com.cliptozip.authsvc.application.port.in.ValidateTokenUseCasePort;
import com.cliptozip.authsvc.application.port.out.TokenCachePort;
import com.cliptozip.authsvc.application.service.JwtService;
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
    public String validate(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing or empty.");
        }

        try {
            final String userEmail = jwtService.extractClaim(token, Claims::getSubject);

            String cachedToken = tokenCachePort.getToken(userEmail);
            if (cachedToken == null) {
                throw new InvalidTokenException("Token not found in cache. Please log in again.");
            }

            if (!cachedToken.equals(token)) {
                throw new InvalidTokenException("Stale token. A newer token has been issued.");
            }

            if (!jwtService.isTokenValid(token, userEmail)) {
                throw new InvalidTokenException("Token is expired or has an invalid signature.");
            }

            log.info("Token successfully validated for user: {}", userEmail);
            return userEmail;

        } catch (Exception e) {
            log.error("An exception occurred during token validation: {}", e.getMessage());
            throw new InvalidTokenException("Token is malformed or invalid.");
        }
    }
}
