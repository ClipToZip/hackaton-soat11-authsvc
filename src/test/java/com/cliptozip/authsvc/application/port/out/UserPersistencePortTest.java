package com.cliptozip.authsvc.application.port.out;

import com.cliptozip.authsvc.adapter.out.persistence.UserPersistencePortImpl;
import com.cliptozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.cliptozip.authsvc.adapter.out.persistence.repository.UserRepository;
import com.cliptozip.authsvc.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserPersistencePortImpl userPersistencePort;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        user = new User(userId.toString(), "Test User", "test@example.com", "rawPassword");
        
        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPasswordHash("encodedPassword");
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User savedUser = userPersistencePort.save(user);

        verify(userRepository).save(any(UserEntity.class));
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void whenCredentialsAreValid_shouldFindByEmailAndPassword() {
        // Given
        String rawPassword = "password123";
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(encoder.matches(rawPassword, userEntity.getPasswordHash())).thenReturn(true);

        // When
        Optional<User> foundUser = userPersistencePort.findByEmailAndPassword("test@example.com", rawPassword);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void whenPasswordIsInvalid_shouldNotFindByEmailAndPassword() {
        // Given
        String wrongPassword = "wrongPassword";
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(encoder.matches(wrongPassword, userEntity.getPasswordHash())).thenReturn(false);

        // When
        Optional<User> foundUser = userPersistencePort.findByEmailAndPassword("test@example.com", wrongPassword);

        // Then
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void whenUserDoesNotExist_shouldNotFindByEmailAndPassword() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userPersistencePort.findByEmailAndPassword("nonexistent@example.com", "anyPassword");

        // Then
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void shouldCheckIfEmailExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userPersistencePort.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }
}
