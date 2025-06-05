package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.websocket.dto.invite.GameInviteRequest;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InviteSender {

    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate redisTemplate;

    private static final String PENDING_INVITE_KEY_PREFIX = "pending_invite:";

    public void send(GameInviteRequest request) {
        String key = PENDING_INVITE_KEY_PREFIX + request.getToUserId();
        redisTemplate.opsForSet().add(key, request.getFromUserId());

        messagingTemplate.convertAndSend(
                WebSocketEndpoint.userInvite(request.getToUserId()),
                request
        );
    }
}
