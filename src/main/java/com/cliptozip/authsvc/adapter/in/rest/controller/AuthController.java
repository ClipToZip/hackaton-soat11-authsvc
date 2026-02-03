package com.cliptozip.authsvc.adapter.in.rest.controller;

import com.cliptozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.cliptozip.authsvc.adapter.in.rest.request.RegisterRequest;
import com.cliptozip.authsvc.adapter.in.rest.request.ValidateTokenRequest;
import com.cliptozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.cliptozip.authsvc.application.port.in.AuthUseCasePort;
import com.cliptozip.authsvc.application.port.in.RegisterUseCasePort;
import com.cliptozip.authsvc.application.port.in.ValidateTokenUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCasePort authUseCase;
    private final RegisterUseCasePort registerUseCase;
    private final ValidateTokenUseCasePort validateTokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(authUseCase.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest req) {
        registerUseCase.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso.");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestBody @Valid ValidateTokenRequest req) {
        // The use case will now throw an InvalidTokenException if the token is invalid,
        // which will be handled by the GlobalExceptionHandler to return a 401.
        // If it succeeds, it returns the user's email, and we return 200 OK.
        validateTokenUseCase.validate(req.token());
        return ResponseEntity.ok().build();
    }
}
