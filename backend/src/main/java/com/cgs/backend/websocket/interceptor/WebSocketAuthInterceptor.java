package com.cgs.backend.websocket.interceptor;

import com.cgs.backend.common.exception.WebSocketException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.security.JwtProvider;
import com.cgs.backend.security.TokenValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new WebSocketException(ResponseCode.ACCESS_TOKEN_MISSING);
            }

            String token = authHeader.substring(7);
            TokenValidationResult result = jwtProvider.validateToken(token);

            if (result != TokenValidationResult.VALID) {
                if (result == TokenValidationResult.EXPIRED) {
                    throw new WebSocketException(ResponseCode.EXPIRED_ACCESS_TOKEN);
                } else if (result == TokenValidationResult.INVALID_SIGNATURE) {
                    throw new WebSocketException(ResponseCode.INVALID_ACCESS_TOKEN_SIGNATURE);
                } else {
                    throw new WebSocketException(ResponseCode.MALFORMED_ACCESS_TOKEN);
                }
            }

            String userId = jwtProvider.getUserId(token);
            accessor.setUser(() -> userId);
            accessor.getSessionAttributes().put("userId", userId);
        }
        return message;
    }
}
