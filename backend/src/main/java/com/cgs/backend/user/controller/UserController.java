package com.cgs.backend.user.controller;

import com.cgs.backend.global.response.CustomResponse;
import com.cgs.backend.global.response.CustomResponseEntity;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.auth.dto.TokenResponse;
import com.cgs.backend.user.dto.UserLoginRequest;
import com.cgs.backend.user.dto.UserSignUpRequest;
import com.cgs.backend.user.service.UserLoginService;
import com.cgs.backend.user.service.UserSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserSignUpService userSignUpService;
    private final UserLoginService userLoginService;

    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<Void>> signUp(@RequestBody @Valid UserSignUpRequest request) {
        userSignUpService.signUp(request);
        return CustomResponseEntity.success(ResponseCode.USER_CREATE_SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<TokenResponse>> login(@RequestBody @Valid UserLoginRequest request) {
        return CustomResponseEntity.success(ResponseCode.USER_LOGIN_SUCCESS, userLoginService.login(request));
    }
}
