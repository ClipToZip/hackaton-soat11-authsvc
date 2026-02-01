package com.clicktozip.authsvc.application.port.in;

import com.clicktozip.authsvc.application.exception.InvalidTokenException;

public interface ValidateTokenUseCasePort {
    /**
     * Validates a JWT token.
     * @param token The JWT token string.
     * @return The user's email (subject) if the token is valid.
     * @throws InvalidTokenException if the token is invalid for any reason.
     */
    String validate(String token);
}
