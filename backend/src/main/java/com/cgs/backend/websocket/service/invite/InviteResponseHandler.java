package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.invite.GameInviteResponse;
import com.cgs.backend.websocket.service.online.OnlineUserService;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InviteResponseHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate redisTemplate;
    private final OnlineUserService onlineUserService;

    private static final String PENDING_INVITE_KEY_PREFIX = "pending_invite:";

    public void handleInviteResponse(GameInviteResponse response) {
        String key = PENDING_INVITE_KEY_PREFIX + response.getToUserId();

        if (Boolean.TRUE.equals(response.getAccepted())) {
            onlineUserService.updateOnlineUserStatus(response.getToUserId(), UserStatus.IN_GAME);
            onlineUserService.updateOnlineUserStatus(response.getFromUserId(), UserStatus.IN_GAME);
            onlineUserService.broadcastOnlineUsers();

            Set<String> fromUserIds = redisTemplate.opsForSet().members(key);
            if (fromUserIds != null) {
                for (String otherFromUserId : fromUserIds) {
                    if (!otherFromUserId.equals(response.getFromUserId())) {
                        messagingTemplate.convertAndSend(
                                WebSocketEndpoint.userInviteResponse(otherFromUserId),
                                new GameInviteResponse(
                                        response.getToUserId(),
                                        null,
                                        otherFromUserId,
                                        null,
                                        false,
                                        response.getToNickname() + "님이 초대를 거절했습니다."
                                )
                        );
                    }
                }
            }

            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(response.getFromUserId()),
                    new GameInviteResponse(
                            response.getFromUserId(),
                            response.getFromNickname(),
                            response.getToUserId(),
                            response.getToNickname(),
                            true,
                            response.getToNickname() + "님이 초대를 수락했습니다."
                    )
            );
        } else {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(response.getFromUserId()),
                    new GameInviteResponse(
                            response.getFromUserId(),
                            response.getFromNickname(),
                            response.getToUserId(),
                            response.getToNickname(),
                            false,
                            response.getToNickname() + "님이 초대를 거절했습니다."
                    )
            );
        }
        redisTemplate.delete(key);
    }
}
