package com.cgs.backend.global.security;

import java.security.Principal;
import java.util.Objects;

public class StompPrincipal implements Principal {
    private final String userId;

    public StompPrincipal(String userId) {
        this.userId = userId;
    }

    @Override    public String getName() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StompPrincipal)) return false;
        StompPrincipal that = (StompPrincipal) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "StompPrincipal{" + "name='" + userId + '\'' + '}';
    }
}
