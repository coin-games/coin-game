package com.cgs.backend.user.service;

import com.cgs.backend.global.exception.UserException;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.user.entity.UserRecord;
import com.cgs.backend.user.repository.UserRecordRepository;
import com.cgs.backend.websocket.dto.game.GameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.attribute.UnmodifiableSetException;

@Service
@RequiredArgsConstructor
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;

    public void applyGameResult(String userId, GameResult gameResult) {
        UserRecord record = userRecordRepository.findById(userId).
                orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        if (GameResult.WIN == gameResult) {
            record.addWin();
        } else if (GameResult.LOSE == gameResult) {
            record.addLoss();
        } else if (GameResult.DRAW == gameResult) {
            return;
        }

        userRecordRepository.save(record);
    }
}
