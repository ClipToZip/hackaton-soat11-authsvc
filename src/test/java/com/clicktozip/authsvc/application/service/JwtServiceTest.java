package com.clicktozip.authsvc.application.service;

import com.clicktozip.authsvc.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "thisIsAValidTestSecretThatIsLongEnoughForHS256";
    private final long testExpirationSeconds = 3600L;
    private Key testKey;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(testSecret, testExpirationSeconds);
        testKey = Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void shouldGenerateValidJwtToken() {
        // Given
        User user = new User("user-id", "Test User", "test@example.com", "password");

        // When
        String token = jwtService.generateToken(user);

        // Then
        assertThat(token).isNotNull().isNotEmpty();
        // Verify that the token can be parsed with the same key, which confirms it's a valid JWT
        assertThat(Jwts.parserBuilder().setSigningKey(testKey).build().isSigned(token)).isTrue();
    }

    @Test
    void shouldContainCorrectClaimsInGeneratedToken() {
        // Given
        User user = new User("user-id", "Test User", "test@example.com", "password");
        Instant now = Instant.now();

        // When
        String token = jwtService.generateToken(user);

        // Then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(testKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("test@example.com");
        assertThat(claims.getIssuedAt()).isCloseTo(Date.from(now), 1000); // within 1 second
        assertThat(claims.getExpiration()).isCloseTo(Date.from(now.plusSeconds(testExpirationSeconds)), 1000);
    }

    @Test
    void shouldReturnCorrectExpirationSeconds() {
        // When
        long expiration = jwtService.getExpirationSeconds();

        // Then
        assertThat(expiration).isEqualTo(testExpirationSeconds);
    }
}
