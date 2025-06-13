package com.cgs.backend.websocket.service.manager;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.global.exception.UserException;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.user.entity.User;
import com.cgs.backend.user.entity.UserRecord;
import com.cgs.backend.user.repository.UserRecordRepository;
import com.cgs.backend.user.repository.UserRepository;
import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.dto.game.GameResult;
import com.cgs.backend.websocket.dto.online.OnlineUserMessage;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineUserManager {

    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final ObjectMapper objectMapper;
    private final WebSocketUtils webSocketUtils;
    private final RedisRepository redisRepository;

    public void saveOnlineUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        UserRecord record = userRecordRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.RECORD_NOT_FOUND));

        OnlineUserMessage dto = new OnlineUserMessage(user.getUserId(), user.getNickname(), UserStatus.WAITING, record.getWins(), record.getLosses());
        try {
            String json = objectMapper.writeValueAsString(dto);
            redisRepository.setValue(RedisKeys.ONLINE_USER_PREFIX + userId, json);
        } catch (JsonProcessingException e) {
            throw new UserException(ResponseCode.REDIS_SERIALIZATION_ERROR);
        }
    }

    public List<OnlineUserMessage> getAllOnlineUsers() {
        Set<String> keys = redisRepository.getKeysByPattern(RedisKeys.ONLINE_USER_PREFIX + "*");
        if (keys.isEmpty()) return Collections.emptyList();

        return keys.stream()
                .map(redisRepository::getValue)
                .filter(Objects::nonNull)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, OnlineUserMessage.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void updateOnlineUserStatus(String userId, UserStatus status) {
        String key = RedisKeys.ONLINE_USER_PREFIX + userId;
        String json = redisRepository.getValue(key);

        if (json == null) throw new UserException(ResponseCode.USER_NOT_FOUND);

        try {
            OnlineUserMessage user = objectMapper.readValue(json, OnlineUserMessage.class);
            user.setStatus(status);

            String updatedJson = objectMapper.writeValueAsString(user);
            redisRepository.setValue(key, updatedJson);
        } catch (JsonProcessingException e) {
            throw new UserException(ResponseCode.REDIS_SERIALIZATION_ERROR);
        }
    }

    public void broadcastOnlineUsers() {
        List<OnlineUserMessage> users = getAllOnlineUsers();
        webSocketUtils.publishMessage(WebSocketEndpoint.onlineUsers(), users);
    }

    public UserStatus getUserStatus(String userId) {
        OnlineUserMessage user = getOnlineUserById(userId);
        if (user == null) throw new UserException(ResponseCode.USER_NOT_FOUND);
        return user.getStatus();
    }

    private OnlineUserMessage getOnlineUserById(String userId) {
        String key = RedisKeys.ONLINE_USER_PREFIX + userId;
        String json = redisRepository.getValue(key);
        if (json == null) return null;

        try {
            return objectMapper.readValue(json, OnlineUserMessage.class);
        } catch (JsonProcessingException e) {
            throw new UserException(ResponseCode.REDIS_SERIALIZATION_ERROR);
        }
    }

    //redis 정리 - online_user: 승패 증가
    public void incrementUserResult(String userId, GameResult gameResult) {
        String key = RedisKeys.ONLINE_USER_PREFIX + userId;
        String json = redisRepository.getValue(key);

        if (json == null) throw new UserException(ResponseCode.USER_NOT_FOUND);

        try {
            OnlineUserMessage user = objectMapper.readValue(json, OnlineUserMessage.class);

            if (gameResult == GameResult.WIN) user.setWins(user.getWins() + 1);
            else if (gameResult == GameResult.LOSE) user.setLosses(user.getLosses() + 1);
            else if (gameResult == GameResult.DRAW) return;

            String updatedJson = objectMapper.writeValueAsString(user);
            redisRepository.setValue(key, updatedJson);
        } catch (JsonProcessingException e) {
            throw new UserException(ResponseCode.REDIS_SERIALIZATION_ERROR);
        }
    }
}
