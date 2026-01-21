package com.clicktozip.authsvc.adapter.out.persistence;

import com.clicktozip.authsvc.adapter.out.mapper.UserMapper;
import com.clicktozip.authsvc.adapter.out.persistence.repository.UserRepository;
import com.clicktozip.authsvc.application.port.out.UserPersistencePort;
import com.clicktozip.authsvc.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPersistencePortImpl implements UserPersistencePort {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userRepository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
