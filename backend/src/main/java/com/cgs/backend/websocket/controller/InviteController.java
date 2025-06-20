package com.cgs.backend.websocket.controller;

import com.cgs.backend.websocket.dto.invite.GameInviteMessage;
import com.cgs.backend.websocket.dto.invite.GameInviteResponseMessage;
import com.cgs.backend.websocket.service.invite.InviteResponseService;
import com.cgs.backend.websocket.service.invite.InviteSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InviteController {

    private final InviteSendService inviteSenderService;
    private final InviteResponseService inviteResponseService;

    @MessageMapping("/game/invite")
    public void inviteGame(@Payload GameInviteMessage message) {
        inviteSenderService.sendInvite(message);
    }

    @MessageMapping("/game/invite/response")
    public void respondInvite(@Payload GameInviteResponseMessage message) {
        inviteResponseService.handleInviteResponse(message);
    }
}
