package com.cgs.backend.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Id
    private String id;
    private String nickname;
    private int score;
    private LocalDateTime recordedAt;

    @Builder
    public Score(String nickname, int score) {
        this.id = "SCORE_" + UUID.randomUUID().toString().substring(0, 16);
        this.nickname = nickname;;
        this.score = score;
        this.recordedAt = LocalDateTime.now();
    }
}
