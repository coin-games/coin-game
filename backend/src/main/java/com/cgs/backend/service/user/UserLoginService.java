package com.cgs.backend.service.user;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.dto.user.UserLoginRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import com.cgs.backend.security.JwtProvider;
import com.cgs.backend.service.redis.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    public TokenResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        validatePassword(request, user);

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        saveRefreshToken(user.getId(), refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(String userId, String refreshToken) {
        long refreshExpiration = jwtProvider.getRefreshTokenExpiration();
        String redisKey = "refresh:" + userId;
        tokenService.saveRefreshToken(redisKey, refreshToken, refreshExpiration);
    }

    private void validatePassword(UserLoginRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(ResponseCode.INVALID_PASSWORD);
        }
    }
}
