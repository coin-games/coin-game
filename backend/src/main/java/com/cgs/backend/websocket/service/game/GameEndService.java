package com.cgs.backend.websocket.service.game;

import com.cgs.backend.websocket.dto.game.GameEndMessage;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final WebSocketUtils webSocketUtils;

    public void endGame(GameEndMessage message) {
        //브로드캐스트
        webSocketUtils.publishMessage(WebSocketEndpoint.gameEnd(message.getRoomId()), message);

        //redis 정리 -> 사용자 waiting 상태로 변경

        //게임 기록 DB 저장 or 유저 상태 update(redis 승패/)
    }
}
