package com.cgs.backend.controller;

import com.cgs.backend.common.exception.UserException;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.dto.user.UserLoginRequest;
import com.cgs.backend.dto.user.UserSignUpRequest;
import com.cgs.backend.service.user.UserLoginService;
import com.cgs.backend.service.user.UserSignUpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserSignUpService userSignUpService;

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserSignUpService userSignUpService() {
            return mock(UserSignUpService.class);
        }
        @Bean
        public UserLoginService userLoginService() {
            return mock(UserLoginService.class);
        }
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @BeforeEach
    void setUp() {
        reset(userSignUpService, userLoginService);
    }

    @Test
    void 회원가입_성공_201응답() throws Exception {
        //given
        UserSignUpRequest request = new UserSignUpRequest("woo0", "woo0@gmail.com", "12345678");

        //when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("회원가입에 성공했습니다."));
    }

    @Test
    void 회원가입_검증실패_400응답() throws Exception {
        //given
        UserSignUpRequest invalidRequest = new UserSignUpRequest("woo0", "woo0@gmail.m", "1234");

        //when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 로그인_성공_200_응답() throws Exception {
        UserLoginRequest request = new UserLoginRequest( "woo0@gmail.com", "12345678");
        TokenResponse tokenResponse = new TokenResponse("access-token", "refresh-token");

        given(userLoginService.login(any())).willReturn(tokenResponse);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인에 성공했습니다."))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    void 로그인_실패_존재하지_않는_유저_401응답() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest("woo0", "woo0@gmail.com", "12345678");

        given(userLoginService.login(any()))
                .willThrow(new UserException(ResponseCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }
}