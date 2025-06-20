package com.cgs.backend.user.controller;

import com.cgs.backend.global.response.CustomResponse;
import com.cgs.backend.global.response.CustomResponseEntity;
import com.cgs.backend.global.response.ResponseCode;
import com.cgs.backend.user.dto.UserRankingResponse;
import com.cgs.backend.user.service.UserRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {

    private final UserRankingService userRankingService;

    @GetMapping("/score")
    public ResponseEntity<CustomResponse<List<UserRankingResponse>>> getRankByScore() {
        List<UserRankingResponse> rankings = userRankingService.getScoreRankings();
        return CustomResponseEntity.success(ResponseCode.RANK_FETCH_SUCCESS, rankings);
    }
}
