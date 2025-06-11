package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.dto.game.GameUpdateMessage;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameUpdateService {

    private final WebSocketUtils webSocketUtils;

    public void updateGame(GameUpdateMessage message) {
        webSocketUtils.publishMessage(WebSocketEndpoint.gameUpdate(message.getRoomId()), message);
    }
}
