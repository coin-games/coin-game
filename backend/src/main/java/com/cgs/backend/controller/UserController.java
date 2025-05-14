package com.cgs.backend.controller;

import com.cgs.backend.common.response.CustomResponse;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.dto.user.UserLoginRequest;
import com.cgs.backend.dto.user.UserSignUpRequest;
import com.cgs.backend.service.user.UserLoginService;
import com.cgs.backend.service.user.UserSignUpService;
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
        return ResponseEntity.ok(CustomResponse.success(ResponseCode.USER_CREATE_SUCCESS));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<TokenResponse>> login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(CustomResponse.success(ResponseCode.USER_LOGIN_SUCCESS, userLoginService.login(request)));
    }
}
