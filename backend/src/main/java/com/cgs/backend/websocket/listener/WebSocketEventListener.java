package com.cgs.backend.websocket.listener;

import com.cgs.backend.websocket.constants.RedisKeys;
import com.cgs.backend.websocket.repository.RedisRepository;
import com.cgs.backend.websocket.service.game.GameDisconnectService;
import com.cgs.backend.websocket.service.manager.OnlineUserManager;
import com.cgs.backend.websocket.util.WebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final OnlineUserManager onlineUserManager;
    private final RedisRepository redisRepository;
    private final GameDisconnectService gameDisconnectService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            onlineUserManager.saveOnlineUser(userId);
        }
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (WebSocketEndpoint.onlineUsers().equals(destination)) {
            onlineUserManager.broadcastOnlineUsers();
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            //강제 종료 감지 -> 게임 중이면 패배 처리
            gameDisconnectService.handleUserDisconnected(userId);

            //redis online_user 삭제
            redisRepository.deleteValue(RedisKeys.ONLINE_USER_PREFIX + userId);
            onlineUserManager.broadcastOnlineUsers();
        }
    }
}