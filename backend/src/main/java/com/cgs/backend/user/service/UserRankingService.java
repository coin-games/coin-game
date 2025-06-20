package com.cgs.backend.user.service;

import com.cgs.backend.user.dto.UserRankingResponse;
import com.cgs.backend.user.entity.Score;
import com.cgs.backend.user.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRankingService {

    private final ScoreRepository scoreRepository;

    public List<UserRankingResponse> getScoreRankings() {
        List<Score> scores = scoreRepository.findTop100ByOrderByScoreDesc();

        return scores.stream()
                .map(score -> {
                    return new UserRankingResponse(score.getNickname(), score.getScore());
                } )
                .toList();
    }
}