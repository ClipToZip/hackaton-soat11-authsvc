package com.cliptozip.authsvc.application.service;

import com.cliptozip.authsvc.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "thisIsAValidTestSecretThatIsLongEnoughForHS256";
    private final long testExpirationSeconds = 3600L;
    private Key testKey;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(testSecret, testExpirationSeconds);
        testKey = Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8));
        testUser = new User("user-id", "Test User", "test@example.com", "password");
    }

    @Nested
    @DisplayName("Token Generation")
    class TokenGenerationTests {
        @Test
        void shouldGenerateValidJwtToken() {
            String token = jwtService.generateToken(testUser);
            assertThat(token).isNotNull().isNotEmpty();
            assertThat(Jwts.parserBuilder().setSigningKey(testKey).build().isSigned(token)).isTrue();
        }

        @Test
        void shouldContainCorrectClaims() {
            Instant now = Instant.now();
            String token = jwtService.generateToken(testUser);
            Claims claims = Jwts.parserBuilder().setSigningKey(testKey).build().parseClaimsJws(token).getBody();

            assertThat(claims.getSubject()).isEqualTo(testUser.getEmail());
            assertThat(claims.getIssuedAt()).isCloseTo(Date.from(now), 1000);
            assertThat(claims.getExpiration()).isCloseTo(Date.from(now.plusSeconds(testExpirationSeconds)), 1000);
        }
    }

    @Nested
    @DisplayName("Token Validation")
    class TokenValidationTests {
        @Test
        void whenTokenIsValid_shouldReturnTrue() {
            String token = jwtService.generateToken(testUser);
            boolean isValid = jwtService.isTokenValid(token, testUser.getEmail());
            assertThat(isValid).isTrue();
        }

        @Test
        void whenTokenIsExpired_shouldReturnFalse() {
            // Manually create an expired token
            String expiredToken = Jwts.builder()
                    .setSubject(testUser.getEmail())
                    .setIssuedAt(Date.from(Instant.now().minus(2, ChronoUnit.HOURS)))
                    .setExpiration(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))
                    .signWith(testKey, SignatureAlgorithm.HS256)
                    .compact();

            boolean isValid = jwtService.isTokenValid(expiredToken, testUser.getEmail());
            assertThat(isValid).isFalse();
        }

        @Test
        void whenTokenSubjectDoesNotMatch_shouldReturnFalse() {
            String token = jwtService.generateToken(testUser);
            boolean isValid = jwtService.isTokenValid(token, "another.user@example.com");
            assertThat(isValid).isFalse();
        }

        @Test
        void whenTokenIsMalformed_shouldReturnFalse() {
            boolean isValid = jwtService.isTokenValid("not-a-valid-jwt", testUser.getEmail());
            assertThat(isValid).isFalse();
        }

        @Test
        void whenTokenHasInvalidSignature_shouldReturnFalse() {
            Key anotherKey = Keys.hmacShaKeyFor("anotherSecretKeyThatIsAlsoVeryLongAndSecure123".getBytes(StandardCharsets.UTF_8));
            String tokenWithWrongSignature = Jwts.builder()
                    .setSubject(testUser.getEmail())
                    .signWith(anotherKey, SignatureAlgorithm.HS256)
                    .compact();

            boolean isValid = jwtService.isTokenValid(tokenWithWrongSignature, testUser.getEmail());
            assertThat(isValid).isFalse();
        }
    }

    @Test
    void shouldReturnCorrectExpirationSeconds() {
        assertThat(jwtService.getExpirationSeconds()).isEqualTo(testExpirationSeconds);
    }
}
