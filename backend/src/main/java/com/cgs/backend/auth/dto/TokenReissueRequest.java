package com.cgs.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenReissueRequest {
    private String accessToken;
    private String refreshToken;
}
