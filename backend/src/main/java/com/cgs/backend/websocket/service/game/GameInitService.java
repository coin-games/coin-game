package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.dto.game.GameInitMessage;
import com.cgs.backend.websocket.dto.game.GameInitResponseMessage;
import com.cgs.backend.websocket.dto.game.GameScoreMessage;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.service.manager.GameRoomManager;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameInitService {

    private final WebSocketUtils webSocketUtils;
    private final GameRoomManager gameRoomManager;

    public void initGame(GameInitMessage message) {
        String opponentId = gameRoomManager.getOpponent(message.getRoomId(), message.getUserId());

        int opponentScore = gameRoomManager.getScore(message.getRoomId(), opponentId);
        int selfScore = gameRoomManager.getScore(message.getRoomId(), message.getUserId());

        //상대에게 내 초기 게임보드 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameInit(opponentId), new GameInitResponseMessage(message.getInitialBoard()));

        //나에게 2 플레이어의 초기 점수 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameScore(message.getUserId()), new GameScoreMessage(selfScore, opponentScore));
    }
}
