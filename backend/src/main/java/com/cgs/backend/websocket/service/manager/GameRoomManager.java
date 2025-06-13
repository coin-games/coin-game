package com.cgs.backend.websocket.service.manager;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomManager {

    private final RedisRepository redisRepository;

    public String createRoom(String userA, String userB) {
        String roomId = UUID.randomUUID().toString().substring(0, 16);
        redisRepository.putToHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerA", userA);
        redisRepository.putToHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerB", userB);

        redisRepository.putToHash(RedisKeys.GAME_SCORE_PREFIX + roomId + ":" + userA, "score", "0");
        redisRepository.putToHash(RedisKeys.GAME_SCORE_PREFIX + roomId + ":" + userB, "score", "0");
        return roomId;
    }

    public String getOpponent(String roomId, String userId) {
        String playerA = redisRepository.getFromHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerA");
        String playerB = redisRepository.getFromHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerB");
        return userId.equals(playerA) ? playerB : playerA;
    }

    public int getScore(String roomId, String userId) {
        String key = RedisKeys.GAME_SCORE_PREFIX + roomId + ":" + userId;
        String score = redisRepository.getFromHash(key, "score");
        return score != null ? Integer.parseInt(score) : 0;
    }

    public void incrementScore(String roomId, String userId, int delta) {
        String key = RedisKeys.GAME_SCORE_PREFIX + roomId + ":" + userId;
        redisRepository.incrementHashValue(key, "score", delta);
    }
}
