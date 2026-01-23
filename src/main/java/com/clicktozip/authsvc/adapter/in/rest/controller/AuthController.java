package com.clicktozip.authsvc.adapter.in.rest.controller;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.request.RegisterRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.clicktozip.authsvc.application.port.in.AuthUseCasePort;
import com.clicktozip.authsvc.application.port.in.RegisterUseCasePort;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(authUseCase.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest req) {
        registerUseCase.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso.");
    }
}
