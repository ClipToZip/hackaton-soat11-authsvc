package com.clicktozip.authsvc.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidateTokenRequest(
        @NotBlank(message = "Token não pode estar em branco")
        @NotNull(message = "Token não pode ser nula")
        String token
) {
}
