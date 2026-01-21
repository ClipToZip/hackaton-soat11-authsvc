package com.clicktozip.authsvc.application.port.out;

import com.clicktozip.authsvc.adapter.out.persistence.UserPersistencePortImpl;
import com.clicktozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.clicktozip.authsvc.adapter.out.persistence.repository.UserRepository;
import com.clicktozip.authsvc.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistencePortTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPersistencePortImpl userPersistencePort;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        user = new User(userId.toString(), "Test User", "test@example.com", "password");
        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
    }

    @Test
    void shouldSaveUser() {
        // This test requires UserMapper.toEntity() to exist
        // and UserPersistencePortImpl to have a constructor.
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User savedUser = userPersistencePort.save(user);

        verify(userRepository).save(any(UserEntity.class));
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void shouldFindByEmail() {
        // This test requires UserPersistencePortImpl to have a constructor.
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userPersistencePort.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldCheckIfEmailExists() {
        // This test requires UserPersistencePortImpl to have a constructor.
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userPersistencePort.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }
}
