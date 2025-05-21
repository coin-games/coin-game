package com.cgs.backend.common.exception;

import com.cgs.backend.common.response.ResponseCode;

public class WebSocketException extends BaseException {
    public WebSocketException(ResponseCode code) {
        super(code);
    }
}
