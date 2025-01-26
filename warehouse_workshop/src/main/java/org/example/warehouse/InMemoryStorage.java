package org.example.warehouse;

import org.example.warehouse.exception.ItemNotFoundException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryStorage implements Storage {

    private static final int INITIAL_CAPACITY = 256;

    public final Map<String, Wheel> items = new HashMap<>(INITIAL_CAPACITY);

    @Override
    public void putItem(Wheel wheel) {
        items.put(wheel.id(), wheel);
    }

    @Override
    public Wheel getItem(String id) throws ItemNotFoundException {
        Wheel wheel = items.get(id);
        if (wheel == null) {
            throw new ItemNotFoundException(id);
        }
        return wheel;
    }

    @Override
    public boolean containsItem(String id) {
        return items.containsKey(id);
    }

    @Override
    public Wheel removeItem(String id) throws ItemNotFoundException {
        Wheel remove = items.remove(id);
        if (remove == null) {
            throw new ItemNotFoundException(id);
        }
        return remove;
    }

    @Override
    public void putAllItem(List<Wheel> items) {
        for (Wheel item : items) {
            putItem(item);
        }

    }

    @Override
    public Map<String, Wheel> getAllItems() {
        return new HashMap<>(items);   // так возвращать безопасно, т.к. нет возможности на стороне клиента задать иное значение.
        // Тут создается копия HashMap, которая выводится на экран, а оригинал инкапсулирован и недоступен клиентской части.
    }

    @Override
    public List<Wheel> getAllItemsSorted(Predicate<Wheel> predicate) {
        return items.values().stream().filter(predicate)
                .sorted(Comparator.comparing(Wheel::model)
                        .thenComparing(Wheel::category)
                        .thenComparing(Wheel::place)
                        .thenComparing(Wheel::id))
                .collect(Collectors.toList());
    }

}
