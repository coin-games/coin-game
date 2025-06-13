package com.cgs.backend.websocket.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameEndMessage {
    private String SelfNickname;
    private String OpponentNickname;
    private String roomId;
    private String userId;
}
