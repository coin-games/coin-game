package com.cgs.backend.service.auth;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.dto.auth.TokenReissueRequest;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.security.JwtProvider;
import com.cgs.backend.security.TokenValidationResult;
import com.cgs.backend.service.redis.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void 토큰_정상_재발급() {
        //given
        String refreshToken = "validRefreshToken";
        String userId = "user123";
        String newAccessToken = "newAccessToken";

        TokenReissueRequest request = new TokenReissueRequest("accessToken", refreshToken);

        given(jwtProvider.validateToken(refreshToken)).willReturn(TokenValidationResult.VALID);
        given(jwtProvider.getUserId(refreshToken)).willReturn(userId);
        given(tokenService.getRefreshToken("refresh:" + userId)).willReturn(refreshToken);
        given(jwtProvider.createAccessToken(userId)).willReturn(newAccessToken);

        //when
        TokenResponse response = authService.reissue(request);

        //then
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void 만료된_RefreshToken_예외() {
        //given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "expiredRefreshToken");
        given(jwtProvider.validateToken("expiredRefreshToken")).willReturn(TokenValidationResult.EXPIRED);

        //when & then
        assertThrows(UserException.class, () -> authService.reissue(request));
    }

    @Test
    void 저장된_Token_불일치시_예외() {
        //given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "refreshToken");
        String userId = "user123";

        given(jwtProvider.validateToken("refreshToken")).willReturn(TokenValidationResult.VALID);
        given(jwtProvider.getUserId("refreshToken")).willReturn(userId);
        given(tokenService.getRefreshToken("refresh:" + userId)).willReturn("다른Token");

        //when & then
        assertThrows(UserException.class, () -> authService.reissue(request));
    }
}