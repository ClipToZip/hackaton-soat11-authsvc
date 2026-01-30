package com.clicktozip.authsvc.adapter.in.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        String name,

        @NotBlank(message = "Email não pode estar em branco")
        @NotNull(message = "Email não pode ser nulo")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha não pode estar em branco")
        @NotNull(message = "Senha não pode ser nula")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password
) {
}
