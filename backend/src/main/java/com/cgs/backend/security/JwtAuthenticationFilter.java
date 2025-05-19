package com.cgs.backend.security;

import com.cgs.backend.common.response.CustomResponse;
import com.cgs.backend.common.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token == null) {
            sendErrorResponse(response, ResponseCode.ACCESS_TOKEN_MISSING);
            return;
        }

        TokenValidationResult result = jwtProvider.validateToken(token);

        switch (result) {
            case VALID -> {
                String userId = jwtProvider.getUserId(token);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            case EXPIRED -> {
                sendErrorResponse(response, ResponseCode.EXPIRED_ACCESS_TOKEN);
                return;
            }
            case INVALID_SIGNATURE -> {
                sendErrorResponse(response, ResponseCode.INVALID_ACCESS_TOKEN_SIGNATURE);
                return;
            }
            case MALFORMED -> {
                sendErrorResponse(response, ResponseCode.MALFORMED_ACCESS_TOKEN);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, ResponseCode responseCode) throws IOException {
        response.setStatus(responseCode.getHttpStatusCode());
        response.setContentType("application/json;charset=UTF-8");

        CustomResponse<Void> errorResponse = CustomResponse.error(responseCode);
        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
