package com.clicktozip.authsvc.application.usecase;

import com.clicktozip.authsvc.adapter.in.rest.request.RegisterRequest;
import com.clicktozip.authsvc.application.exception.EmailAlreadyExistsException;
import com.clicktozip.authsvc.application.port.in.RegisterUseCasePort;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUseCase implements RegisterUseCasePort {
    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userPersistencePort.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already registered: " + request.email());
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User newUser = new User(null, request.name(), request.email(), hashedPassword);

        userPersistencePort.save(newUser);
    }
}
