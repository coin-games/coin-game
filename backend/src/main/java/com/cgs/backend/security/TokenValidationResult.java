package com.cgs.backend.security;

public enum TokenValidationResult {
    VALID,
    EXPIRED,
    MALFORMED,
    INVALID_SIGNATURE,
}
