package com.cgs.backend.service.user;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.user.UserSignUpRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpRequest userSignUpRequest) {
        validateDuplicateEmail(userSignUpRequest.getEmail());

        User user = User.builder()
                .nickname(userSignUpRequest.getNickname())
                .email(userSignUpRequest.getEmail())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .build();

        userRepository.save(user);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(ResponseCode.USER_EMAIL_ALREADY_EXIST);
        }
    }

}
