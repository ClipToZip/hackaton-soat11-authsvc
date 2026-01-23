package com.clicktozip.authsvc.application.usecase;

import com.clicktozip.authsvc.adapter.in.rest.request.LoginRequest;
import com.clicktozip.authsvc.adapter.in.rest.response.TokenResponse;
import com.clicktozip.authsvc.application.exception.InvalidCredentialsException;
import com.clicktozip.authsvc.application.port.in.AuthUseCasePort;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.application.service.JwtService;
import com.clicktozip.authsvc.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthUseCasePort {
    private final UserPersistencePort userPersistencePort;
    private final JwtService jwtService;

    @Override
    public TokenResponse login(LoginRequest request) {
        User u = userPersistencePort.findByEmailAndPassword(request.email(), request.password())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(u);
        return new TokenResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }
}
