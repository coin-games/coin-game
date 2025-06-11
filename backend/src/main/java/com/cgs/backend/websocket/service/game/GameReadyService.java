package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.dto.game.GameStartMessage;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameReadyService {

    private final RedisRepository redisRepository;
    private final WebSocketUtils webSocketUtils;

    private static final int REQUIRED_READY_COUNT = 2;

    public void readyGame(String roomId, String userId) {
        String key = RedisKeys.GAME_READY_PREFIX + roomId;

        // 사용자의 게임 준비 상태를 Redis의 game_ready:{roomId} 키에 Set({userId, ...}) 형태로 저장
        redisRepository.addToSet(key, userId);
        Long readyCount = redisRepository.getSetSize(key);

        // 해당 Set의 크기가 2가 되면 게임 시작 메시지를 발행한 뒤 해당 키를 삭제
        if (readyCount != null && readyCount == REQUIRED_READY_COUNT) {
            webSocketUtils.publishMessage(WebSocketEndpoint.gameStart(roomId), new GameStartMessage(key));
            redisRepository.deleteFromSet(key);
        }
    }
}
