package com.cgs.backend.common.response;

import org.springframework.http.ResponseEntity;

public class CustomResponseEntity {
    public static <T> ResponseEntity<CustomResponse<T>> success(ResponseCode code, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .body(CustomResponse.success(code, data));
    }

    public static ResponseEntity<CustomResponse<Void>> success(ResponseCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(CustomResponse.success(code));
    }
}
