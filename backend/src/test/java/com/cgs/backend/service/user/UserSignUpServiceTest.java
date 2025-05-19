package com.cgs.backend.service.user;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.dto.user.UserSignUpRequest;
import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserSignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserSignUpService userSignUpService;

    @Test
    void 회원가입_성공() {
        //given
        UserSignUpRequest request = new UserSignUpRequest("woo0", "woo0@gmail.com", "12345678");

        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.empty());
        given(passwordEncoder.encode(request.getPassword()))
                .willReturn("encodedPassword");

        //when
        userSignUpService.signUp(request);

        //then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 이메일_중복시_예외발생() {
        //given
        UserSignUpRequest request = new UserSignUpRequest("woo0", "woo0@gmail.com", "12345678");
        User user = User.builder().nickname("woo1").email("woo0@gmail.com").password("encoded1234").build();
        given(userRepository.findByEmail(request.getEmail()))
                .willReturn(Optional.of(user));

        //when & then
        assertThrows(UserException.class, () -> userSignUpService.signUp(request));
        verify(userRepository, never()).save(any());
    }
}