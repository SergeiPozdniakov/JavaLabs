package org.example.students;

import java.util.*;

import java.util.HashMap;
import java.util.Map;

public class AverageScoreCache {
    private final Map<String, Double> cache = new LRUCache<>(2);

    public Double get(String subject) {
        return cache.get(subject);
    }

    public void put(String subject, double average) {
        cache.put(subject, average);
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(String subject) {
        return cache.containsKey(subject);
    }

class LRUCache<KEY, VALUE> extends LinkedHashMap<KEY, VALUE> {

    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<KEY, VALUE> eldest) {
        return size() > capacity;
    }
    }
}
