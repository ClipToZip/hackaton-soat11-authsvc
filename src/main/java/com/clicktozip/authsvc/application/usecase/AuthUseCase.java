package com.clicktozip.authsvc.application.usecase;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.clicktozip.authsvc.application.exception.InvalidCredentialsException;
import com.clicktozip.authsvc.application.port.in.AuthUseCasePort;
import com.clicktozip.authsvc.application.port.out.TokenCachePort;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.application.service.JwtService;
import com.clicktozip.authsvc.domain.model.User;
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

        // 2. Check for an existing token in the cache
        String cachedToken = tokenCachePort.getToken(user.getEmail());
        if (cachedToken != null) {
            // 3. If a token exists, validate it
            if (jwtService.isTokenValid(cachedToken, user.getEmail())) {
                log.info("Returning valid token from cache for user: {}", user.getEmail());
                return new TokenResponse(cachedToken, "Bearer", jwtService.getExpirationSeconds());
            } else {
                // 4. If token is invalid/expired, remove it from cache
                log.info("Removing expired token from cache for user: {}", user.getEmail());
                tokenCachePort.deleteToken(user.getEmail());
            }
        }

        // 5. If no valid token was found in cache, generate a new one
        log.info("Generating new token for user: {}", user.getEmail());
        String newToken = jwtService.generateToken(user);
        long expirationSeconds = jwtService.getExpirationSeconds();

        // 6. Store the new token in the cache
        tokenCachePort.cacheToken(user.getEmail(), newToken, expirationSeconds, TimeUnit.SECONDS);

        return new TokenResponse(newToken, "Bearer", expirationSeconds);
    }
}
