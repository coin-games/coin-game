package com.cgs.backend.global.exception;

import com.cgs.backend.global.response.ResponseCode;

public class UserException extends BaseException{
    public UserException(ResponseCode code) {
        super(code);
    }
}
