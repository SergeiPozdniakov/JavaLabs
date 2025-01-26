package org.example.warehouse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CachedAnalitics implements Analytics{

    private final Map<CategoryAndPlace, Integer> cache = new LRUCache<>(2);

    private final BasicAnalytics basicAnalitics;

    public CachedAnalitics(BasicAnalytics basicAnalytics) {
        this.basicAnalitics = basicAnalytics;
    }

    @Override
    public Set<String> getCategories() {
        return Set.of();
    }

    @Override
    public Map<CategoryAndPlace, Integer> getAggregationByCategoryAndPlace() {
        return basicAnalitics.getAggregationByCategoryAndPlace();
    }

    @Override
    public Integer getAggregationByCategoryAndPlace(CategoryAndPlace categoryAndPlace) {
        return cache.computeIfAbsent(categoryAndPlace, basicAnalitics::getAggregationByCategoryAndPlace);
    }

    @Override
    public Integer getTotalCount() {
        return basicAnalitics.getTotalCount();
    }
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