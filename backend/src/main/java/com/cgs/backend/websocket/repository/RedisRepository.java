package com.cgs.backend.websocket.repository;

public interface RedisRepository {

    void addToSet(String key, String value);
    Long getSetSize(String key);
    void deleteFromSet(String key);
}
