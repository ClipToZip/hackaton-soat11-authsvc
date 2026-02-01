package com.clicktozip.authsvc.adapter.out.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisTokenCacheAdapterTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisTokenCacheAdapter redisTokenCacheAdapter;

    @Test
    void shouldCallSetOnCacheToken() {
        // Given
        String key = "test@example.com";
        String token = "jwt-token";
        long timeout = 3600;
        TimeUnit unit = TimeUnit.SECONDS;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations); // Stubbing moved here

        // When
        redisTokenCacheAdapter.cacheToken(key, token, timeout, unit);

        // Then
        verify(valueOperations).set(key, token, timeout, unit);
    }

    @Test
    void shouldCallGetOnGetToken() {
        // Given
        String key = "test@example.com";
        String expectedToken = "jwt-token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations); // Stubbing moved here
        when(valueOperations.get(key)).thenReturn(expectedToken);

        // When
        String actualToken = redisTokenCacheAdapter.getToken(key);

        // Then
        verify(valueOperations).get(key);
        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @Test
    void shouldCallDeleteOnDeleteToken() {
        // Given
        String key = "test@example.com";
        // No stubbing is needed for this test

        // When
        redisTokenCacheAdapter.deleteToken(key);

        // Then
        verify(redisTemplate).delete(key);
    }
}
