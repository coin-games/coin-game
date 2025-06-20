package com.cgs.backend.websocket.controller;

import com.cgs.backend.websocket.dto.game.GameEndMessage;
import com.cgs.backend.websocket.dto.game.GameInitMessage;
import com.cgs.backend.websocket.dto.game.GameReadyMessage;
import com.cgs.backend.websocket.dto.game.GameUpdateMessage;
import com.cgs.backend.websocket.service.game.GameEndService;
import com.cgs.backend.websocket.service.game.GameInitService;
import com.cgs.backend.websocket.service.game.GameUpdateService;
import com.cgs.backend.websocket.service.game.GameReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameInitService gameInitService;
    private final GameUpdateService gameUpdateService;
    private final GameReadyService gameReadyService;
    private final GameEndService gameEndService;

    @MessageMapping("/game/ready")
    public void handleReady(@Payload GameReadyMessage message) {
        gameReadyService.readyGame(message.getRoomId(), message.getUserId());
    }

    @MessageMapping("/game/init")
    public void handleInit(@Payload GameInitMessage message) {
        gameInitService.initGame(message);
    }

    @MessageMapping("/game/update")
    public void handleUpdate(@Payload GameUpdateMessage message) {
        gameUpdateService.updateGame(message);
    }

    @MessageMapping("/game/end")
    public void handleEnd(@Payload GameEndMessage message) {
        gameEndService.endGame(message);
    }
}
