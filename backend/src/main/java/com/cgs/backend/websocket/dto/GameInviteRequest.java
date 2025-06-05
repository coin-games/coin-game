package com.cgs.backend.websocket.dto;

import lombok.Getter;

@Getter
public class GameInviteRequest {
    private String fromUserId;
    private String fromNickname;
    private String toUserId;
    private String toNickname;
}
