package com.clicktozip.authsvc.application.port.in;

public interface ValidateTokenUseCasePort {
    /**
     * Validates a JWT token.
     * @param token The JWT token string.
     * @return true if the token is valid, false otherwise.
     */
    boolean validate(String token);
}
