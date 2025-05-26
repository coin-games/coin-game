package com.cgs.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private String id;
    private String nickname;
    private String email;
    private String password;
    private LocalDateTime registeredAt;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserRecord userRecord;

    @Builder
    public User(String nickname, String email, String password) {
        this.id = "USER_" + UUID.randomUUID().toString().substring(0, 16);
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.registeredAt = LocalDateTime.now();
    }
}
