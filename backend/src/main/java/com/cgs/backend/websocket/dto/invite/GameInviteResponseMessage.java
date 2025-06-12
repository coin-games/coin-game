package com.cgs.backend.websocket.dto.invite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameInviteResponseMessage {
    private String fromUserId;
    private String fromNickname;
    private String toUserId;
    private String toNickname;
    private boolean accepted;
    private String roomId;
    private String message;

    public GameInviteResponseMessage(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public static GameInviteResponseMessage accept(GameInviteResponseMessage message, String roomId) {
        return new GameInviteResponseMessage(message.getFromUserId(), message.getFromNickname(), message.getToUserId(), message.getToNickname(), true, roomId, message.getToNickname() + "님이 초대를 수락했습니다.");
    }

    public static GameInviteResponseMessage rejected(GameInviteResponseMessage message) {
        return new GameInviteResponseMessage(message.getFromUserId(), message.getFromNickname(), message.getToUserId(), message.getToNickname(), false, null, message.getToNickname() + "님이 초대를 거절했습니다.");
    }

    public static GameInviteResponseMessage withRoomId(String roomId) {
        return new GameInviteResponseMessage(null, null, null, null, true, roomId, "RoomId 입니다.");
    }
}
