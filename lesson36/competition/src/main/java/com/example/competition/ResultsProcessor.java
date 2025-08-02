package com.example.competition;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("ResultsProcessor")
public class ResultsProcessor {
    private final List<Athlete> athletes = new ArrayList<>();

    public void loadResults(String filePath) throws IOException {
        athletes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                athletes.add(parseAthlete(line));
            }
        }
    }

    private Athlete parseAthlete(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }

        String fullName = parts[0].trim();
        String gender = parts[1].trim();
        String distance = parts[2].trim();
        String timeStr = parts[3].trim();

        String[] timeParts = timeStr.split(":");
        if (timeParts.length != 2) {
            throw new IllegalArgumentException("Invalid time format: " + timeStr);
        }

        int minutes = Integer.parseInt(timeParts[0]);
        int seconds = Integer.parseInt(timeParts[1]);
        int totalSeconds = minutes * 60 + seconds;

        Athlete athlete = new Athlete();
        athlete.setFullName(fullName);
        athlete.setGender(gender);
        athlete.setDistance(distance);
        athlete.setTimeInSeconds(totalSeconds);
        return athlete;
    }

    public List<Athlete> getTopNRunners(int n, String distance, String gender) {
        return athletes.stream()
                .filter(a -> a.getDistance().equals(distance))
                .filter(a -> a.getGender().equals(gender))
                .sorted(Comparator.comparingInt(Athlete::getTimeInSeconds))
                .limit(n)
                .collect(Collectors.toList());
    }
}
