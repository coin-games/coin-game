package com.cgs.backend.service.auth;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.auth.TokenReissueRequest;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.security.JwtProvider;
import com.cgs.backend.service.redis.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    public TokenResponse reissue(TokenReissueRequest request) {
        validateRefreshToken(request.getRefreshToken());

        String userId = jwtProvider.getUserId(request.getRefreshToken());
        validateStoredRefreshToken(userId, request.getRefreshToken());

        String newAccessToken = jwtProvider.createAccessToken(userId);
        return new TokenResponse(newAccessToken, request.getRefreshToken());
    }

    private void validateStoredRefreshToken(String userId, String providedToken) {
        String savedRefreshToken = tokenService.getRefreshToken("refresh:" + userId);
        if (savedRefreshToken == null || !savedRefreshToken.equals(providedToken)) {
            throw new UserException(ResponseCode.INVALID_REFRESH_TOKEN);
        }
    }

    private void validateRefreshToken(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new UserException(ResponseCode.INVALID_REFRESH_TOKEN);
        }
    }
}
