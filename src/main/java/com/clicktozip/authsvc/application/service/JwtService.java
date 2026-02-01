package com.clicktozip.authsvc.application.service;

import com.clicktozip.authsvc.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final Key key;
    private final long expirationSeconds;

    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
            .setSubject(user.getEmail())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(exp))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String token, String userEmail) {
        try {
            final String subject = extractClaim(token, Claims::getSubject);
            return (subject.equals(userEmail) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
