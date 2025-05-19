package com.cgs.backend.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String key, String refreshToken, long expiresIn) {
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expiresIn));
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
