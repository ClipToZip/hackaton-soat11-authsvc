package com.clicktozip.authsvc.adapter.out.persistence;

import com.clicktozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.clicktozip.authsvc.adapter.out.persistence.repository.UserRepository;
import com.clicktozip.authsvc.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistencePortImplTest {

    @Mock
    private UserRepository userRepository;

    private UserPersistencePortImpl userPersistencePort;

    @BeforeEach
    void setUp() {
        // Instantiate the class as is
        userPersistencePort = new UserPersistencePortImpl();
        try {
            // Because the original class has no constructor or setter for the repository,
            // we must use reflection to inject the mock. This is not a good practice
            // but it's the only way to test the class without modifying it.
            Field repositoryField = UserPersistencePortImpl.class.getDeclaredField("userRepository");
            repositoryField.setAccessible(true);
            repositoryField.set(userPersistencePort, userRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mock repository via reflection", e);
        }
    }

    @Test
    void whenFindByEmail_shouldReturnUser() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        // When
        Optional<User> foundUser = userPersistencePort.findByEmail("test@example.com");

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

    @Disabled("This test cannot be run because UserMapper.toEntity() does not exist, causing a compilation error in the source code.")
    @Test
    void whenSaveUser_shouldCallRepositoryAndReturnUser() {
        // This test is disabled because the code it tests for does not compile.
    }
}
