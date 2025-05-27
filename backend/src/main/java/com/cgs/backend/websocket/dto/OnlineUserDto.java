package com.cgs.backend.websocket.dto;

import com.cgs.backend.global.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OnlineUserDto {
    private String userId;
    private String nickname;
    private UserStatus status;
    private int wins;
    private int losses;
}
