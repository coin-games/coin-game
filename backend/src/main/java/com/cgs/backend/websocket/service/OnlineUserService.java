package com.cgs.backend.websocket.service;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.global.exception.UserException;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.user.entity.User;
import com.cgs.backend.user.entity.UserRecord;
import com.cgs.backend.user.repository.UserRecordRepository;
import com.cgs.backend.user.repository.UserRepository;
import com.cgs.backend.websocket.dto.OnlineUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineUserService {

    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveOnlineUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        UserRecord record = userRecordRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.RECORD_NOT_FOUND));

        OnlineUserDto dto = new OnlineUserDto(user.getId(), user.getNickname(), UserStatus.WAITING, record.getWins(), record.getLosses());

        try {
            String json = objectMapper.writeValueAsString(dto);
            redisTemplate.opsForValue().set("online_user:" + userId, json);
        } catch (JsonProcessingException e) {
            throw new UserException(ResponseCode.REDIS_SERIALIZATION_ERROR);
        }
    }

    public List<OnlineUserDto> getAllOnlineUsers() {
        Set<String> keys = redisTemplate.keys("online_user:*");
        if (keys.isEmpty()) return Collections.emptyList();

        return keys.stream()
                .map(redisTemplate.opsForValue()::get)
                .filter(Objects::nonNull)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, OnlineUserDto.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
