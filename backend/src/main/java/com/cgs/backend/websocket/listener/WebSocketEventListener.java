package com.cgs.backend.websocket.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final StringRedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) accessor.getSessionAttributes().get("userId");
        if (userId != null) {
            redisTemplate.opsForSet().add("online_users", userId);
            broadcastOnlineUsers();
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) accessor.getSessionAttributes().get("userId");
        if (userId != null) {
            redisTemplate.opsForSet().remove("online_users", userId);
            broadcastOnlineUsers();
        }
    }

    private void broadcastOnlineUsers() {
        Set<String> users = redisTemplate.opsForSet().members("online_users");
        messagingTemplate.convertAndSend("/topic/online-users", users);
    }
}