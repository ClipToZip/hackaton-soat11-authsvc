package com.clicktozip.authsvc.adapter.out.persistence;

import com.clicktozip.authsvc.adapter.out.mapper.UserMapper;
import com.clicktozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.clicktozip.authsvc.adapter.out.persistence.repository.UserRepository;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistencePortImpl implements UserPersistencePort {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public User save(User user) {
        UserEntity entityToSave = UserMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(entityToSave);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent() && encoder.matches(password, user.get().getPasswordHash())) {
            return user.map(UserMapper::toDomain);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
