package com.cgs.backend.auth.controller;

import com.cgs.backend.global.response.CustomResponse;
import com.cgs.backend.global.response.CustomResponseEntity;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.auth.dto.TokenReissueRequest;
import com.cgs.backend.auth.dto.TokenResponse;
import com.cgs.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<CustomResponse<TokenResponse>> reissue(@RequestBody TokenReissueRequest request) {
        return CustomResponseEntity.success(ResponseCode.ACCESS_TOKEN_REISSUE_SUCCESS, authService.reissue(request));
    }

}
