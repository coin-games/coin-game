package com.cgs.backend.user.service;

import com.cgs.backend.global.exception.UserException;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.auth.dto.TokenResponse;
import com.cgs.backend.user.dto.UserLoginRequest;
import com.cgs.backend.user.entity.User;
import com.cgs.backend.global.security.JwtProvider;
import com.cgs.backend.auth.service.RefreshTokenService;
import com.cgs.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        validatePassword(request, user);

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        saveRefreshToken(user.getId(), refreshToken);
        return new TokenResponse(accessToken, refreshToken, user.getEmail(), user.getUserRecord().getWins(), user.getUserRecord().getLosses());
    }

    private void saveRefreshToken(String userId, String refreshToken) {
        long refreshExpiration = jwtProvider.getRefreshTokenExpiration();
        String redisKey = "refresh:" + userId;
        refreshTokenService.saveRefreshToken(redisKey, refreshToken, refreshExpiration);
    }

    private void validatePassword(UserLoginRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(ResponseCode.INVALID_PASSWORD);
        }
    }
}
