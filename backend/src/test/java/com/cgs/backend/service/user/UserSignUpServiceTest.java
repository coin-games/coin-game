package com.cgs.backend.service.user;

import com.cgs.backend.entity.User;
import com.cgs.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserSignUpServiceTest {

    @Autowired private UserSignUpService userSignUpService;
    @Autowired private UserRepository userRepository;

    @Test
    void 회원가입() {
        //given
        User unSignedUser = User.builder()
                .nickname("참치형")
                .email("jung12")
                .password("1234")
                .build();
        //when

        //then

    }

    @Test
    void 주복_회원가입_예외() {
        //given
        User user = User.builder()
                .nickname("참치형")
                .email("jung12")
                .password("1234")
                .build();

        //when

        //then
    }


}