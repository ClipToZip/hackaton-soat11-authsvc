package com.cliptozip.authsvc.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldInstantiateWithNoArgsConstructorAndSetData() {
        // Given
        User user = new User();
        String userId = "user-123";
        String name = "John Doe";
        String email = "john.doe@example.com";
        String passwordHash = "securepassword";


        // When
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPassswordHash(passwordHash);


        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPassswordHash());
    }

    @Test
    void shouldInstantiateWithAllArgsConstructor() {
        // Given
        String userId = "user-456";
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        String passwordHash = "anotherhash";


        // When
        User user = new User(userId, name, email, passwordHash);

        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPassswordHash());
    }

    @Test
    void shouldAllowNullValuesInSetters() {
        // Given
        User user = new User("id", "name", "email", "password");

        // When
        user.setUserId(null);
        user.setName(null);
        user.setEmail(null);
        user.setPassswordHash(null);

        // Then
        assertNull(user.getUserId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassswordHash());
    }
}
