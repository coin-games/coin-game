package com.cgs.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingResponse {
    private String nickname;
    private int score;
}
