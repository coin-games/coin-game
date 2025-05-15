package com.cgs.backend.service.user;

import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.dto.user.UserLoginRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import com.cgs.backend.security.JwtProvider;
import com.cgs.backend.service.redis.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserLoginService userLoginService;

    @Test
    void 로그인_성공() {
        //given
        UserLoginRequest request = new UserLoginRequest("woo0@gmail.com", "12345678");
        User user = User.builder()
                .email("woo0@gmail.com")
                .password("encodedPassword")
                .nickname("woo0")
                .build();

        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .willReturn(true);
        given(jwtProvider.createAccessToken(user))
                .willReturn("accessToken");
        given(jwtProvider.createRefreshToken(user))
                .willReturn("refreshToken");
        given(jwtProvider.getRefreshTokenExpiration())
                .willReturn(3600000L);

        //when
        TokenResponse response = userLoginService.login(request);

        //then
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(tokenService).saveRefreshToken("refresh:" + user.getId(), "refreshToken", 3600000L);
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtProvider).createAccessToken(user);
        verify(jwtProvider).createRefreshToken(user);
    }
}