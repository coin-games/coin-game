package com.cgs.backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String email;
    private int wins;
    private int losses;
}
