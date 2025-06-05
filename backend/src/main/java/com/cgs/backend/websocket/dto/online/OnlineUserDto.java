package com.cgs.backend.websocket.dto.online;

import com.cgs.backend.global.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OnlineUserDto {
    private String userId;
    private String nickname;
    private UserStatus status;
    private int wins;
    private int losses;
}
