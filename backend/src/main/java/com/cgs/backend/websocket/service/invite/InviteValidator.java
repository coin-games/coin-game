package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.invite.GameInviteRequest;
import com.cgs.backend.websocket.dto.invite.GameInviteResponse;
import com.cgs.backend.websocket.service.online.OnlineUserService;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InviteValidator {

    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserService onlineUserService;

    public boolean validateInvite(GameInviteRequest request) {
        if (request.getFromUserId().equals(request.getToUserId())) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, "자기 자신을 초대할 수 없습니다.")
            );
            return false;
        }

        UserStatus userStatus = onlineUserService.getUserStatus(request.getToUserId());

        if (userStatus == UserStatus.OFFLINE) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, request.getToNickname() + "님은 오프라인입니다.")
            );
            return false;
        } else if (userStatus == UserStatus.IN_GAME) {
            messagingTemplate.convertAndSend(
                    WebSocketEndpoint.userInviteResponse(request.getFromUserId()),
                    new GameInviteResponse(false, request.getToNickname() + "님은 게임 중입니다.")
            );
            return false;
        }
        return true;
    }
}
