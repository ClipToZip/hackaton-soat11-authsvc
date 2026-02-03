package com.cliptozip.authsvc.adapter.out.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void shouldInstantiateAndSetDataCorrectly() {
        // Given
        UserEntity userEntity = new UserEntity();
        UUID userId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        // When
        userEntity.setUserId(userId);
        userEntity.setName("Jane Doe");
        userEntity.setEmail("jane.doe@example.com");
        userEntity.setPasswordHash("securepassword");
        userEntity.setCreatedAt(now);

        // Then
        assertEquals(userId, userEntity.getUserId());
        assertEquals("Jane Doe", userEntity.getName());
        assertEquals("jane.doe@example.com", userEntity.getEmail());
        assertEquals("securepassword", userEntity.getPasswordHash());
        assertEquals(now, userEntity.getCreatedAt());
    }

    @Test
    void shouldInstantiateWithAllArgsConstructor() {
        // Given
        UUID userId = UUID.randomUUID();
        String name = "John Smith";
        String email = "john.smith@example.com";
        String passwordHash = "anotherhash";
        OffsetDateTime createdAt = OffsetDateTime.now();

        // When
        UserEntity userEntity = new UserEntity(userId, name, email, passwordHash, createdAt);

        // Then
        assertEquals(userId, userEntity.getUserId());
        assertEquals(name, userEntity.getName());
        assertEquals(email, userEntity.getEmail());
        assertEquals(passwordHash, userEntity.getPasswordHash());
        assertEquals(createdAt, userEntity.getCreatedAt());
    }

    @Test
    void shouldHaveDefaultCreatedAtValueWhenInstantiatedWithNoArgsConstructor() {
        // When
        UserEntity userEntity = new UserEntity();

        // Then
        assertNotNull(userEntity.getCreatedAt());
    }
}
