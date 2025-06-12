package com.cgs.backend.websocket.service.room;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final RedisRepository redisRepository;

    public String createRoom(String userA, String userB) {
        String roomId = UUID.randomUUID().toString().substring(0, 16);
        redisRepository.putToHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerA", userA);
        redisRepository.putToHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerB", userB);
        return roomId;
    }

    public String getOpponent(String roomId, String userId) {
        String playerA = redisRepository.getFromHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerA");
        String playerB = redisRepository.getFromHash(RedisKeys.GAME_ROOM_PREFIX + roomId, "playerB");
        return userId.equals(playerA) ? playerB : playerA;
    }
}
