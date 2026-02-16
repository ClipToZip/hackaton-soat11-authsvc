package com.cliptozip.authsvc.application.usecase;

import com.cliptozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.cliptozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.cliptozip.authsvc.application.exception.InvalidCredentialsException;
import com.cliptozip.authsvc.application.port.in.AuthUseCasePort;
import com.cliptozip.authsvc.application.port.out.TokenCachePort;
import com.cliptozip.authsvc.application.port.out.UserPersistencePort;
import com.cliptozip.authsvc.application.service.JwtService;
import com.cliptozip.authsvc.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthUseCasePort {
    private final UserPersistencePort userPersistencePort;
    private final JwtService jwtService;
    private final TokenCachePort tokenCachePort;

    @Override
    public TokenResponse login(LoginRequest request) {

        // 1. Authenticate user credentials first
        User user = userPersistencePort.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        try {
            // 2. Check for an existing token in the cache
            String cachedToken = tokenCachePort.getToken(user.getUserId());
            if (cachedToken != null) {
                // 3. If a token exists, validate it
                if (jwtService.isTokenValid(cachedToken, user.getUserId())) {
                    log.info("Returning valid token from cache for user: {}", user.getUserId());
                    return new TokenResponse(cachedToken, "Bearer", jwtService.getExpirationSeconds());
                } else {
                    // 4. If token is invalid/expired, remove it from cache
                    log.info("Removing expired token from cache for user: {}", user.getUserId());
                    tokenCachePort.deleteToken(user.getUserId());
                }
            }

            // 5. If no valid token was found in cache, generate a new one
            log.info("Generating new token for user: {}", user.getUserId());
            String newToken = jwtService.generateToken(user);
            long expirationSeconds = jwtService.getExpirationSeconds();

            // 6. Store the new token in the cache
            tokenCachePort.cacheToken(user.getUserId(), newToken, expirationSeconds, TimeUnit.SECONDS);
            return new TokenResponse(newToken, "Bearer", expirationSeconds);
        } catch (Exception e) {
            log.error("Unexpected error occurred during login: {}", e.getMessage(), e);
            // Throw a generic exception to be caught by the global handler
            throw new RuntimeException("An unexpected error occurred during login.");
        }
    }
}
