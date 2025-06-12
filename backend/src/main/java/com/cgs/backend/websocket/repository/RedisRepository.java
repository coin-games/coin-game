package com.cgs.backend.websocket.repository;

import java.util.Set;

public interface RedisRepository {

    //set
    void addToSet(String key, String value);
    Long getSetSize(String key);
    void deleteFromSet(String key);
    Set<String> getAllFromSet(String key);

    //hash
    void putToHash(String key, String field, String value);
    String getFromHash(String key, String field);
}
