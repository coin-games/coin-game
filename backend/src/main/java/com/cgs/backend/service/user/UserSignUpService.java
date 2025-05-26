package com.cgs.backend.service.user;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.user.UserSignUpRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.entity.UserRecord;
import com.cgs.backend.repository.UserRecordRepository;
import com.cgs.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignUpRequest userSignUpRequest) {
        validateDuplicateEmail(userSignUpRequest.getEmail());

        User user = User.builder()
                .nickname(userSignUpRequest.getNickname())
                .email(userSignUpRequest.getEmail())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .build();

        userRepository.save(user);

        // 전적 0승 0패로 초기화
        UserRecord userRecord = UserRecord.builder()
                .userId(user.getId())
                .build();

        userRecordRepository.save(userRecord);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(ResponseCode.USER_EMAIL_ALREADY_EXIST);
        }
    }

}
