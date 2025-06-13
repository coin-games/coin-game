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
    Long incrementHashValue(String key, String field, long delta);

    //value (map의)
    void setValue(String key, String value);
    String getValue(String key);
    void deleteValue(String key);

    //key
    Set<String> getKeysByPattern(String pattern);
}
