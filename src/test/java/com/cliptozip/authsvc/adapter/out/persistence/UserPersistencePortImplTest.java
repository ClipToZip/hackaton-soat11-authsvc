package com.cliptozip.authsvc.adapter.out.persistence;

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
class UserPersistencePortImplTest {

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
        user = new User(userId.toString(), "Test User", "test@example.com", "hashedPassword");
        
        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setPasswordHash("hashedPassword");
    }

    @Test
    void whenSaveUser_shouldCallRepositoryAndReturnMappedUser() {
        // Given
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // When
        User savedUser = userPersistencePort.save(user);

        // Then
        verify(userRepository).save(any(UserEntity.class));
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassswordHash()).isNull();
    }

    @Test
    void whenFindByEmail_shouldReturnUser() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(encoder.matches("pass123", "hashedPassword")).thenReturn(true);

        // When
        Optional<User> foundUser = userPersistencePort.findByEmailAndPassword("test@example.com", "pass123");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void whenExistsByEmail_shouldReturnTrue() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When
        boolean exists = userPersistencePort.existsByEmail("test@example.com");

        // Then
        assertThat(exists).isTrue();
    }
}
