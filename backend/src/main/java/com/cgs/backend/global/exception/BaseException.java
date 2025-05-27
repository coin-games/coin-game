package com.cgs.backend.global.exception;

import com.cgs.backend.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private final ResponseCode code;

    @Override
    public String getMessage() {
        return code.getMessage();
    }

    public BaseException(ResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
