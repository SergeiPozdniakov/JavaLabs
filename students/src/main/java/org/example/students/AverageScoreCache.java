package org.example.students;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

public class AverageScoreCache {
    private final Map<String, Double> cache = new HashMap<>();

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
}
