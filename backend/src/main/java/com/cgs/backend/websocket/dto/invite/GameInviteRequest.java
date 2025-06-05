package com.cgs.backend.websocket.dto.invite;

import lombok.Getter;

@Getter
public class GameInviteRequest {
    private String fromUserId;
    private String fromNickname;
    private String toUserId;
    private String toNickname;
}
