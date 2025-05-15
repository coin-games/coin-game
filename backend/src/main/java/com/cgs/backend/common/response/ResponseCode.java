package com.cgs.backend.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ResponseCode {

    // 400 validation
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),

    // 401 Unauthorized
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "저장된 Refresh Token과 일치하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다."),
    INVALID_REFRESH_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "Refresh Token의 서명이 올바르지 않습니다."),
    MALFORMED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token의 형식이 올바르지 않습니다."),
    ACCESS_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "Access Token이 제공되지 않았습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "Access Token의 서명이 올바르지 않습니다."),
    MALFORMED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token 형식이 잘못되었습니다."),

    // 409 Conflict
    USER_EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다."),

    // 200 OK
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    ACCESS_TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "Access Token이 재발급되었습니다."),

    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다.");

    private final HttpStatus status;
    private final String message;

    public int getHttpStatusCode() {
        return status.value();
    }

}
