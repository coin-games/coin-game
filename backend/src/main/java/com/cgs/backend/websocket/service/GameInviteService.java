

package com.cgs.backend.websocket.service;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.GameInviteRequest;
import com.cgs.backend.websocket.dto.GameInviteResponse;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameInviteService {

    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserService onlineUserService;
    private final StringRedisTemplate redisTemplate;

    private static final String PENDING_INVITE_KEY_PREFIX = "pending_invite:";

    public void sendInvite(GameInviteRequest request) {
        if (request.getFromUserId().equals(request.getToUserId())) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, "자기 자신을 초대할 수 없습니다.")
            );
            return;
        }

        UserStatus userStatus = onlineUserService.getUserStatus(request.getToUserId());

        if (userStatus == UserStatus.OFFLINE) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, request.getToNickname() + "님은 오프라인입니다.")
            );
            return;
        } else if (userStatus == UserStatus.IN_GAME) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, request.getToNickname() + "님은 게임 중입니다.")
            );
            return;
        }

        String key = PENDING_INVITE_KEY_PREFIX + request.getToUserId();
        redisTemplate.opsForSet().add(key, request.getFromUserId());

        messagingTemplate.convertAndSend(
                WebSocketEndpoint.userInvite(request.getToUserId()),
                request
        );
    }

    public void processInviteResponse(GameInviteResponse response) {
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
