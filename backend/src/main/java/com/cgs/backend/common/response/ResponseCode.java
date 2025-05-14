package com.cgs.backend.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ResponseCode {

    // 200 OK
    USER_LOGIN_SUCCESS(HttpStatus.OK, "사용자 로그인 성공"),

    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, "사용자 회원가입 성공");

    private final HttpStatus status;
    private final String message;

    public int getHttpStatusCode() {
        return status.value();
    }

}
