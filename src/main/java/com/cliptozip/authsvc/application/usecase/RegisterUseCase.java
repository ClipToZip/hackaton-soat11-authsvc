package com.cliptozip.authsvc.application.usecase;

import com.cliptozip.authsvc.adapter.in.rest.request.RegisterRequest;
import com.cliptozip.authsvc.application.exception.EmailAlreadyExistsException;
import com.cliptozip.authsvc.application.port.in.RegisterUseCasePort;
import com.cliptozip.authsvc.application.port.out.UserPersistencePort;
import com.cliptozip.authsvc.domain.model.User;
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
