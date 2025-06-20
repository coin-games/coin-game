package com.cgs.backend.user.service;

import com.cgs.backend.user.entity.Score;
import com.cgs.backend.user.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreSaveService {

    private final ScoreRepository scoreRepository;

    public void saveUserScore(String nickname, int scoreValue) {
        Score score = Score.builder()
                .nickname(nickname)
                .score(scoreValue)
                .build();
        scoreRepository.save(score);
    }
}
