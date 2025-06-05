

package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.invite.GameInviteRequest;
import com.cgs.backend.websocket.dto.invite.GameInviteResponse;
import com.cgs.backend.websocket.service.online.OnlineUserService;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameInviteService {

    private final InviteValidator inviteValidator;
    private final InviteSender inviteSender;
    private final InviteResponseHandler inviteResponseHandler;

    public void sendInvite(GameInviteRequest request) {
        if (!inviteValidator.validateInvite(request)) return;
        inviteSender.send(request);
        //초대 전송 로직 (Redis 저장 + 메시지 전송)
    }

    public void processInviteResponse(GameInviteResponse response) {
        inviteResponseHandler.handleInviteResponse(response);
        //사용자의 수락,거절 응답 처리 (초대자에게 응답 전송 포함)
    }
}
