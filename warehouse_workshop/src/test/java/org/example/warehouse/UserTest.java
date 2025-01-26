package org.example.warehouse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class UserTest {

    private final Storage storage = new InMemoryStorage();

    @Test
    void getAllModelsSorted() {
        Wheel wheel1 = new Wheel("1", "model1", "winter", "A", 10);
        Wheel wheel2 = new Wheel("2", "model2", "winter", "A", 10);
        Wheel wheel3 = new Wheel("3", "model3", "winter", "A", 10);
        Wheel wheel4 = new Wheel("4", "model2", "summer", "A", 10);
        Wheel wheel5 = new Wheel("5", "model3", "summer", "B", 10);
        List<String> allModelsSorted = List.of("model1", "model2", "model3");
        storage.putAllItem(List.of(wheel1, wheel2, wheel3, wheel4, wheel5));

        Map<String, Wheel> allItems = storage.getAllItems();

        Set<String> sortedOrNot = allItems.values().stream()
                .map(Wheel::model)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Assertions.assertEquals(allModelsSorted, List.copyOf(sortedOrNot));
    }

    @Test
    void buildUploadFromCsv() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/wheels.csv")))) {

            String headerLine = reader.readLine();
            String[] header = headerLine.split(",");
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                headerMap.put(header[i], i);
            }
            //System.out.println(headerMap);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                Wheel wheel = new Wheel(UUID.randomUUID().toString(),
                        split[headerMap.get("model")],
                        split[headerMap.get("category")],
                        split[headerMap.get("place")],
                        Integer.parseInt(split[headerMap.get("quantity")])
                );
                storage.putItem(wheel);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(10, storage.getAllItems().size());
       storage.getAllItems().values().forEach(System.out::println);


    }
}
