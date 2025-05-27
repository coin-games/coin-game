package com.cgs.backend.global.security;

public enum TokenValidationResult {
    VALID,
    EXPIRED,
    MALFORMED,
    INVALID_SIGNATURE,
}
