package org.example.warehouse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BasicAnalytics implements Analytics {


    private Storage storage;

    public BasicAnalytics(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Set<String> getCategories() {
        Set<String> catygories = new HashSet<>();
        for (Wheel wheel : storage.getAllItems().values()) {
            catygories.add(wheel.category());
        }
        return catygories;
        // Один из вариантов реализации
        // return storage.getAllItems().values().stream().map(Wheel::category).collect(Collectors.toSet());
    }

    @Override
    public Map<CategoryAndPlace, Integer> getAggregationByCategoryAndPlace() {
        Map<CategoryAndPlace, Integer> aggregations = new HashMap<>();
        Map<String, Wheel> storedItems = storage.getAllItems();
        for (Wheel wheel : storedItems.values()) {
            CategoryAndPlace categoryAndPlace = new CategoryAndPlace(wheel.category(), wheel.place());
            Integer aggregation = aggregations.getOrDefault(categoryAndPlace, 0);
            aggregation += wheel.quantity();
            aggregations.put(categoryAndPlace, aggregation);
        }

        return aggregations;
    }

    @Override
    public Integer getAggregationByCategoryAndPlace(CategoryAndPlace categoryAndPlace) {
        Integer quantity = 0;
        Map<String, Wheel> storedItems = storage.getAllItems();
        for (Wheel wheel : storedItems.values()) {
            if (wheel.category().equals(categoryAndPlace.category()) && wheel.place().equals(categoryAndPlace.place())) {
                quantity += wheel.quantity();
            }
        }
        return quantity;
    }

    @Override
    public Integer getTotalCount() {
        return storage.getAllItems().values().stream().map(Wheel::quantity).mapToInt(Integer::intValue).sum();
    }
}
