package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.dto.game.GameUpdateMessage;
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
        webSocketUtils.publishMessage(WebSocketEndpoint.gameUpdate(opponentId), message);
    }
}
