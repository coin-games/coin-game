package com.cgs.backend.websocket.util;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketUtils {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishMessage(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}
