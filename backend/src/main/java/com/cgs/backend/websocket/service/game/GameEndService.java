package com.cgs.backend.websocket.service.game;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.game.GameEndMessage;
import com.cgs.backend.websocket.dto.game.GameEndResponseMessage;
import com.cgs.backend.websocket.dto.game.GameResult;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.service.manager.GameRoomManager;
import com.cgs.backend.websocket.service.manager.OnlineUserManager;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final WebSocketUtils webSocketUtils;
    private final GameRoomManager gameRoomManager;
    private final OnlineUserManager onlineUserManager;

    public void endGame(GameEndMessage message) {
        String opponentId = gameRoomManager.getOpponent(message.getRoomId(), message.getUserId());

        int selfScore = gameRoomManager.getScore(message.getRoomId(), message.getUserId());
        int opponentScore = gameRoomManager.getScore(message.getRoomId(), opponentId);

        // 결과 판정
        GameResult selfResult;
        GameResult opponentResult;

        if (selfScore > opponentScore) {
            selfResult = GameResult.WIN;
            opponentResult = GameResult.LOSE;
        } else if (selfScore < opponentScore) {
            selfResult = GameResult.LOSE;
            opponentResult = GameResult.WIN;
        } else {
            selfResult = opponentResult = GameResult.DRAW;
        }

        //나에게 종료 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameEnd(message.getRoomId()), new GameEndResponseMessage(selfResult, selfScore, opponentScore));
        //상대방에게 종료 메시지 발행
        webSocketUtils.publishMessage(WebSocketEndpoint.gameEnd(opponentId), new GameEndResponseMessage(opponentResult, opponentScore, selfScore));

        //redis 정리 - 사용자 상태 waiting으로 변경
        onlineUserManager.updateOnlineUserStatus(message.getUserId(), UserStatus.WAITING);
        onlineUserManager.updateOnlineUserStatus(opponentId, UserStatus.WAITING);
        onlineUserManager.broadcastOnlineUsers();
        //redis 정리 - game_score:, game_room: 삭제
        gameRoomManager.clearRoom(message.getRoomId());

        //게임 기록 DB 저장 or 유저 상태 update(redis 승패/)

    }
}
