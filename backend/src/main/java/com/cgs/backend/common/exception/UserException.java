package com.cgs.backend.common.exception;

import com.cgs.backend.common.response.ResponseCode;

public class UserException extends BaseException{
    public UserException(ResponseCode code) {
        super(code);
    }
}
