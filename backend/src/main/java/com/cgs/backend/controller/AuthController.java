package com.cgs.backend.controller;

import com.cgs.backend.common.response.CustomResponse;
import com.cgs.backend.common.response.ResponseCode;
import com.cgs.backend.dto.auth.TokenReissueRequest;
import com.cgs.backend.dto.user.TokenResponse;
import com.cgs.backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<CustomResponse<TokenResponse>> reissue(@RequestBody TokenReissueRequest request) {
        return ResponseEntity.ok(CustomResponse.success(ResponseCode.ACCESS_TOKEN_REISSUE_SUCCESS, authService.reissue(request)));
    }

}
