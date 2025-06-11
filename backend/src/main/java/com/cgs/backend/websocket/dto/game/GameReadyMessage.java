package com.cgs.backend.websocket.dto.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameReadyMessage {
    private String roomId;
    private String userId;
}
