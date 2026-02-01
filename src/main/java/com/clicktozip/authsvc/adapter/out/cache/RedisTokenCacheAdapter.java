package com.clicktozip.authsvc.adapter.out.cache;

import com.clicktozip.authsvc.application.port.out.TokenCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenCacheAdapter implements TokenCachePort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void cacheToken(String key, String token, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, token, timeout, unit);
    }

    @Override
    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
}
