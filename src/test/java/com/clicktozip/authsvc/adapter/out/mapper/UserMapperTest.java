package com.clicktozip.authsvc.adapter.out.mapper;

import com.clicktozip.authsvc.adapter.out.persistence.entity.UserEntity;
import com.clicktozip.authsvc.domain.model.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void shouldCorrectlyMapUserEntityToUserDomain() {
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity(
                userId,
                "John Doe",
                "john.doe@example.com",
                "hashedpassword",
                OffsetDateTime.now()
        );

        User domainUser = UserMapper.toDomain(entity);

        assertNotNull(domainUser);
        assertEquals(userId.toString(), domainUser.getUserId());
        assertEquals("John Doe", domainUser.getName());
        assertEquals("john.doe@example.com", domainUser.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserEntityToDomainIsNull() {
        User domainUser = UserMapper.toDomain(null);
        assertNull(domainUser);
    }

    @Test
    void shouldCorrectlyMapUserDomainToUserEntity() {
        User user = new User(UUID.randomUUID().toString(), "Jane Doe", "jane.doe@example.com", "password123");

        UserEntity entity = UserMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getUserId(), entity.getUserId().toString());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPassswordHash(), entity.getPasswordHash());
    }

    @Test
    void shouldReturnNullWhenUserDomainToEntityIsNull() {
        UserEntity entity = UserMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void whenToEntityIsCalledWithInvalidUserId_shouldThrowException() {
        // Given
        User userWithInvalidId = new User("not-a-valid-uuid", "Test", "test@test.com", "pass");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            UserMapper.toEntity(userWithInvalidId);
        });
    }
}
