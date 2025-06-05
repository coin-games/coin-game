package com.cgs.backend.websocket.dto.invite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameInviteResponse {
    private String fromUserId;
    private String fromNickname;
    private String toUserId;
    private String toNickname;
    private Boolean accepted;
    private String message;

    public GameInviteResponse(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }
}
