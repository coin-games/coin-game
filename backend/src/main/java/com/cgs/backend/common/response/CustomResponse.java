package com.cgs.backend.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    public static <T> CustomResponse<T> success(ResponseCode code, T data) {
        return new CustomResponse<>(code.getHttpStatusCode(), code.getMessage(), data);
    }

    public static CustomResponse<Void> success(ResponseCode code) {
        return new CustomResponse<>(code.getHttpStatusCode(), code.getMessage(), null);
    }

    public static CustomResponse<Void> error(ResponseCode code) {
        return new CustomResponse<>(code.getHttpStatusCode(), code.getMessage(), null);
    }
}
