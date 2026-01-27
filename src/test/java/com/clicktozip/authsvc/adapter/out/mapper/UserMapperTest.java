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
        // Given
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity(
                userId,
                "John Doe",
                "john.doe@example.com",
                "hashedpassword",
                OffsetDateTime.now()
        );

        // When
        User domainUser = UserMapper.toDomain(entity);

        // Then
        assertNotNull(domainUser);
        assertEquals(userId.toString(), domainUser.getUserId());
        assertEquals("John Doe", domainUser.getName());
        assertEquals("john.doe@example.com", domainUser.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserEntityIsNull() {
        // Given
        UserEntity entity = null;

        // When
        User domainUser = UserMapper.toDomain(entity);

        // Then
        assertNull(domainUser);
    }
}
