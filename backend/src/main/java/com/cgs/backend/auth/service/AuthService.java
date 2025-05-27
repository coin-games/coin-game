package com.cgs.backend.auth.service;

import com.cgs.backend.global.exception.UserException;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.auth.dto.TokenReissueRequest;
import com.cgs.backend.auth.dto.TokenResponse;
import com.cgs.backend.global.security.JwtProvider;
import com.cgs.backend.global.security.TokenValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse reissue(TokenReissueRequest request) {
        //전달받은 refreshToken 검증
        validateRefreshToken(request.getRefreshToken());

        //reids에 저장된 토큰과 비교
        String userId = jwtProvider.getUserId(request.getRefreshToken());
        validateStoredRefreshToken(userId, request.getRefreshToken());

        //검증이 완료되면 accessToken 재발급
        String newAccessToken = jwtProvider.createAccessToken(userId);
        return new TokenResponse(newAccessToken, request.getRefreshToken());
    }

    private void validateStoredRefreshToken(String userId, String providedToken) {
        String savedRefreshToken = refreshTokenService.getRefreshToken("refresh:" + userId);
        if (savedRefreshToken == null || !savedRefreshToken.equals(providedToken)) {
            throw new UserException(ResponseCode.REFRESH_TOKEN_MISMATCH);
        }
    }

    private void validateRefreshToken(String token) {
        TokenValidationResult result = jwtProvider.validateToken(token);

        switch (result) {
            case VALID -> {
                return;
            }
            case EXPIRED -> throw new UserException(ResponseCode.EXPIRED_REFRESH_TOKEN);
            case INVALID_SIGNATURE -> throw new UserException(ResponseCode.INVALID_REFRESH_TOKEN_SIGNATURE);
            case MALFORMED -> throw new UserException(ResponseCode.MALFORMED_REFRESH_TOKEN);
        }
    }
}
