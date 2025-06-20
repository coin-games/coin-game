package com.cgs.backend.websocket.dto.invite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InviteValidationResult {
    private boolean accepted;
    private String message;
}
