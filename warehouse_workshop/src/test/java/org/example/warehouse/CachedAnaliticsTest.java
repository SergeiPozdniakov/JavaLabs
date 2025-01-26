package org.example.warehouse;

import org.example.warehouse.exception.ItemNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class CachedAnaliticsTest {

    private final MockStorage storage = new MockStorage();

    private final Analytics analytics = new CachedAnalitics(new BasicAnalytics(storage));


    @Test
    void callOnceForRepeatedRequest() {
        CategoryAndPlace request = new CategoryAndPlace("winter", "A");

        Integer aggregationByCategoryAndPlace = analytics.getAggregationByCategoryAndPlace(request);

        Assertions.assertEquals(15, aggregationByCategoryAndPlace);

        analytics.getAggregationByCategoryAndPlace(request);
        analytics.getAggregationByCategoryAndPlace(request);
        analytics.getAggregationByCategoryAndPlace(request);

        Assertions.assertEquals(1, storage.calls);
    }

    @Test
    void callManyTimesRepeatedRequest() {
        CategoryAndPlace request1 = new CategoryAndPlace("winter", "A");
        CategoryAndPlace request2 = new CategoryAndPlace("winter", "B");
        CategoryAndPlace request3 = new CategoryAndPlace("summer", "B");

        analytics.getAggregationByCategoryAndPlace(request1);
        analytics.getAggregationByCategoryAndPlace(request2);
        analytics.getAggregationByCategoryAndPlace(request1);

        Assertions.assertEquals(2, storage.calls);

        analytics.getAggregationByCategoryAndPlace(request3);
        Assertions.assertEquals(3, storage.calls);

        analytics.getAggregationByCategoryAndPlace(request2);
        Assertions.assertEquals(4, storage.calls);

    }

private class MockStorage implements Storage {

        private int calls = 0;

        @Override
        public void putItem(Wheel wheel) {

        }

        @Override
        public Wheel getItem(String id) throws ItemNotFoundException {
            return null;
        }

        @Override
        public boolean containsItem(String id) {
            return false;
        }

        @Override
        public Wheel removeItem(String id) throws ItemNotFoundException {
            return null;
        }

        @Override
        public void putAllItem(List<Wheel> items) {

        }

        @Override
        public Map<String, Wheel> getAllItems() {
            calls++;
            Wheel wheel = new Wheel("1", "hakkapelita", "summer", "A", 5);
            Wheel winter = new Wheel("2", "hakkapelita", "winter", "A", 5);
            Wheel winter2 = new Wheel("21", "hakkapelita", "winter", "A", 5);
            Wheel winter3 = new Wheel("22", "hakkapelita", "winter", "A", 5);
            Wheel allSeasons = new Wheel("3", "hakkapelita", "allSeason", "A", 5);
            return Map.of(wheel.id(), wheel, winter.id(), winter, winter2.id(), winter2,winter3.id(), winter3, allSeasons.id(), allSeasons);
        }

        @Override
        public List<Wheel> getAllItemsSorted(Predicate<Wheel> predicate) {
            return List.of();
        }
    };

}