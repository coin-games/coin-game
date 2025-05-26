package com.cgs.backend.websocket.Controller;

import com.cgs.backend.websocket.dto.OnlineUserDto;
import com.cgs.backend.websocket.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OnlineUserController {

    private final OnlineUserService onlineUserService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/online-users")
    public void sendOnlineUsers() {
        List<OnlineUserDto> users = onlineUserService.getAllOnlineUsers();
        messagingTemplate.convertAndSend("/topic/online-users", users);
    }
}
