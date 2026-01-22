package com.clicktozip.authsvc.adapter.in.rest.controller;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.clicktozip.authsvc.application.usecase.AuthUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(authUseCase.login(req));
    }
}
