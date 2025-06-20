package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.dto.game.GameScoreMessage;
import com.cgs.backend.websocket.dto.game.GameUpdateMessage;
import com.cgs.backend.websocket.dto.game.GameUpdateResponseMessage;
import com.cgs.backend.websocket.service.manager.GameRoomManager;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameUpdateService {

    private final WebSocketUtils webSocketUtils;
    private final GameRoomManager gameRoomManager;

    public void updateGame(GameUpdateMessage message) {
        String opponentId = gameRoomManager.getOpponent(message.getRoomId(), message.getUserId());

        //점수 증가: 좌표 개수 x 10
        int scoreDelta = message.getPositions().length * 10;
        gameRoomManager.incrementScore(message.getRoomId(), message.getUserId(), scoreDelta);

        //점수 조회
        int selfScore = gameRoomManager.getScore(message.getRoomId(), message.getUserId());
        int opponentScore = gameRoomManager.getScore(message.getRoomId(), opponentId);

        //상대에게 내가 부순 코인의 자표 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameUpdate(opponentId), new GameUpdateResponseMessage(message.getPositions()));

        //나에게 현재 점수 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameScore(message.getUserId()), new GameScoreMessage(selfScore, opponentScore));
        //상대방에게도 현재 점수 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameScore(opponentId), new GameScoreMessage(opponentScore, selfScore));
    }
}
