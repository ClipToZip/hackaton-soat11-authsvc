package com.clicktozip.authsvc.adapter.in.rest.request;

public record LoginRequest(
    String email,
    String password
) {
}
