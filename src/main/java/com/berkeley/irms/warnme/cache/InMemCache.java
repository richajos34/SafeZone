package com.berkeley.irms.warnme.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * An in-memory cache that stores key-value pairs of type T.
 * The cache provides methods to get, put, and clear the cached values.
 */
public class InMemCache<T> {

    private final Map<String, T> cacheMap = new HashMap<>();

    public T get(String key) {
        return cacheMap.get(key);
    }

    public void put(String key, T value) {
        cacheMap.put(key, value);
    }

    public void clear() {
        cacheMap.clear();
    }
}
