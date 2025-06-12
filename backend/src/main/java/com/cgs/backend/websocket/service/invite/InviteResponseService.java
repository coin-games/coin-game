package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.dto.invite.GameInviteResponseMessage;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.service.manager.OnlineUserManager;
import com.cgs.backend.websocket.service.manager.GameRoomManager;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class InviteResponseService {

    private final RedisRepository redisRepository;
    private final OnlineUserManager onlineUserManager;
    private final GameRoomManager gameRoomManager;
    private final WebSocketUtils webSocketUtils;

    public void handleInviteResponse(GameInviteResponseMessage message) {
        String key = RedisKeys.PENDING_INVITE_PREFIX + message.getToUserId();

        if (message.isAccepted()) {
            // 두 유저의 상태 게임중으로 변경 및 온라인 유저 목록 메시지 발행
            onlineUserManager.updateOnlineUserStatus(message.getToUserId(), UserStatus.IN_GAME);
            onlineUserManager.updateOnlineUserStatus(message.getFromUserId(), UserStatus.IN_GAME);
            onlineUserManager.broadcastOnlineUsers();

            // 특정 사용자가 초대를 수락하면 특정 사용자에게 초대를 보낸 다른 사용자들에게 거절 메시지 발행
            Set<String> fromUserIds = redisRepository.getAllFromSet(key);
            if (fromUserIds != null) {
                // 사용자 초대 거절시 초대자에게 거절 메시지 발행
                for (String otherFromUserId : fromUserIds) {
                    if (!otherFromUserId.equals(message.getFromUserId())) {
                        webSocketUtils.publishMessage(
                                WebSocketEndpoint.userInviteResponse(otherFromUserId),
                                GameInviteResponseMessage.rejected(message)
                        );
                    }
                }
            }

            String roomId = gameRoomManager.createRoom(message.getFromUserId(), message.getToUserId());

            //초대자에게 수락 메시지 발행 (roomId 포함)
            webSocketUtils.publishMessage(
                    WebSocketEndpoint.userInviteResponse(message.getFromUserId()),
                    GameInviteResponseMessage.accept(message, roomId)
            );

            //초대 수락자에게도 roomId 전달
            webSocketUtils.publishMessage(
                    WebSocketEndpoint.userInviteResponse(message.getToUserId()),
                    GameInviteResponseMessage.withRoomId(roomId)
            );

            //게임 시작했으니 초대 전송한 유저 목록 제거
            redisRepository.deleteFromSet(key);
        } else {
            // 사용자 초대 거절시 초대자에게 거절 메시지 발행
            webSocketUtils.publishMessage(
                    WebSocketEndpoint.userInviteResponse(message.getFromUserId()),
                    GameInviteResponseMessage.rejected(message)
            );
        }
    }
}
