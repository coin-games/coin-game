package com.cgs.backend.websocket.service.invite;

import com.cgs.backend.global.enums.UserStatus;
import com.cgs.backend.websocket.dto.invite.GameInviteMessage;
import com.cgs.backend.websocket.dto.invite.InviteValidationResult;
import com.cgs.backend.websocket.service.manager.OnlineUserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InviteValidator {

    private final OnlineUserManager onlineUserManager;

    public InviteValidationResult validate(GameInviteMessage message) {
        if (message.getFromUserId().equals(message.getToUserId())) {
            return new InviteValidationResult(false, "자기 자신을 초대할 수 없습니다.");
        }

        UserStatus status = onlineUserManager.getUserStatus(message.getToUserId());
        if (status == UserStatus.IN_GAME) {
            return new InviteValidationResult( false,message.getToNickname() + "님은 게임 중입니다.");
        }

        return new InviteValidationResult(true, null);
    }
}
