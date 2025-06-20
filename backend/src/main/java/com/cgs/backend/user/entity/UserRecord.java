package com.cgs.backend.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecord {

    @Id
    private String userId;
    private int wins;
    private int losses;

    @Builder
    public UserRecord(String userId) {
        this.userId = userId;
        this.wins = 0;
        this.losses = 0;
    }

    public void addWin() {
        this.wins += 1;
    }

    public void addLoss() {
        this.losses += 1;
    }
}
