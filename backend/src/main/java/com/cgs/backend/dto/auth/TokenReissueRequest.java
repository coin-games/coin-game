package com.cgs.backend.dto.auth;

import lombok.Getter;

@Getter
public class TokenReissueRequest {
    private String accessToken;
    private String refreshToken;
}
