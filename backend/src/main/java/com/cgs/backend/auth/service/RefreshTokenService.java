package com.cgs.backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

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
