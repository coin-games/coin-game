package com.cgs.backend.websocket.service.game;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.game.GameEndMessage;
import com.cgs.backend.websocket.service.manager.GameRoomManager;
import com.cgs.backend.websocket.service.manager.OnlineUserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameDisconnectService {

    private final OnlineUserManager onlineUserManager;
    private final GameRoomManager gameRoomManager;
    private final GameEndService gameEndService;

    public void handleUserDisconnected(String userId) {
        //게임 중이 아니면 return
        if (onlineUserManager.getUserStatus(userId) != UserStatus.IN_GAME) return;

        //게임 중이었던 roomId
        String roomId = gameRoomManager.findRoomIdByUserId(userId);
        if (roomId == null) return;

        String opponentId = gameRoomManager.getOpponent(roomId, userId);
        String selfNickname = onlineUserManager.getOnlineUserById(userId).getNickname();
        String opponentNickname = onlineUserManager.getOnlineUserById(opponentId).getNickname();

        gameEndService.endGame(new GameEndMessage(roomId, userId, selfNickname, opponentNickname, true));
    }
}
