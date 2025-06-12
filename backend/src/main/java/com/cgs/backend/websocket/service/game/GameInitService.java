package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.dto.game.GameInitMessage;
import com.cgs.backend.websocket.service.room.GameRoomService;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameInitService {

    private final WebSocketUtils webSocketUtils;
    private final GameRoomService gameRoomService;

    public void initGame(GameInitMessage message) {
        String opponentId = gameRoomService.getOpponent(message.getRoomId(), message.getUserId());
        webSocketUtils.publishMessage(WebSocketEndpoint.gameInit(opponentId), message);
    }
}
