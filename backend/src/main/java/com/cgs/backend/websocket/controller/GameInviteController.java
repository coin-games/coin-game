package com.cgs.backend.websocket.controller;

import com.cgs.backend.websocket.dto.GameInviteRequest;
import com.cgs.backend.websocket.dto.GameInviteResponse;
import com.cgs.backend.websocket.service.GameInviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameInviteController {

    private final GameInviteService gameInviteService;

    // 초대 요청
    // /app/game/invite 로 요청
    // /queue/{toUserId}/invite 로 전송
    @MessageMapping("/game/invite")
    public void handleGameInvite(@Payload GameInviteRequest request) {
        gameInviteService.sendInvite(request);
    }

    // 초대 응답 (수락/거절)
    // /app/game/invite/response 로 요청
    // /queue/{fromUserId}/invite-response 로 전송
    @MessageMapping("/game/invite/response")
    public void handleInviteResponse(@Payload GameInviteResponse response) {
        gameInviteService.processInviteResponse(response);
    }
}
