package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.dto.invite.GameInviteMessage;
import com.cgs.backend.websocket.dto.invite.GameInviteResponseMessage;
import com.cgs.backend.websocket.dto.invite.InviteValidationResult;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import com.cgs.backend.websocket.util.WebSocketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteSendService {

    private final InviteValidator inviteValidator;
    private final RedisRepository redisRepository;
    private final WebSocketUtils webSocketUtils;

    public void

    sendInvite(GameInviteMessage message) {
        InviteValidationResult result = inviteValidator.validate(message);

        if (!result.isAccepted()) {
            webSocketUtils.publishMessage(
                    WebSocketEndpoint.userInviteResponse(message.getFromUserId()),
                    new GameInviteResponseMessage(false, result.getMessage())
            );
            return;
        }

        String key = RedisKeys.PENDING_INVITE_PREFIX + message.getToUserId();

        redisRepository.addToSet(key, message.getFromUserId());
        webSocketUtils.publishMessage(
                WebSocketEndpoint.userInvite(message.getToUserId()),
                message
        );
    }
}
