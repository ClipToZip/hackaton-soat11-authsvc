package com.clicktozip.authsvc.adapter.in.rest.response;

public record TokenResponse(
    String token,
    String tokenType,
    long expiresInSeconds
) {
}
