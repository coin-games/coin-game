package com.cgs.backend.websocket.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameEndResponseMessage {
    private GameResult result;
    private int selfScore;
    private int opponentScore;
}
