package com.clicktozip.authsvc.application.port.out;

import com.clicktozip.authsvc.domain.model.User;

import java.util.Optional;

public interface UserPersistencePort{
    User save(User user);
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
