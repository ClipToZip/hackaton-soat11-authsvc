package com.cliptozip.authsvc.application.port.out;

import com.cliptozip.authsvc.domain.model.User;

import java.util.Optional;

public interface UserPersistencePort{
    User save(User user);
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
