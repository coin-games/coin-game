package com.cgs.backend.global.exception;

import com.cgs.backend.global.response.ResponseCode;

public class WebSocketException extends BaseException {
    public WebSocketException(ResponseCode code) {
        super(code);
    }
}
