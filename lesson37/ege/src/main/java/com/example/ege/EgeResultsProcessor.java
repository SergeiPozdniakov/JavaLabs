package com.example.ege;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component("ResultsProcessor")
public class EgeResultsProcessor {

    @Value("${scoring.first.group}")
    private int score1to4;

    @Value("${scoring.second.group}")
    private int score5to8;

    @Value("${scoring.third.group}")
    private int score9to10;

    public int calculateTotalScore(String keysResourceName, String answersResourceName) throws IOException {
        Map<Integer, String> keys = loadAnswers(keysResourceName);
        Map<Integer, String> answers = loadAnswers(answersResourceName);

        int totalScore = 0;
        for (int i = 1; i <= 10; i++) {
            String correct = keys.get(i);
            String student = answers.get(i);
            if (correct != null && correct.equals(student)) {
                totalScore += getScoreForQuestion(i);
            }
        }
        return totalScore;
    }

    Map<Integer, String> loadAnswers(String resourcePath) throws IOException {
        Map<Integer, String> answers = new HashMap<>();

        // Загружаем как ресурс из classpath
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new FileNotFoundException("Ресурс не найден: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-", 2); // разбиваем по первому "-"
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String answer = parts[1].trim();
                    answers.put(number, answer);
                }
            }
        }
        return answers;
    }

    int getScoreForQuestion(int questionNumber) {
        if (questionNumber >= 1 && questionNumber <= 4) {
            return score1to4;
        } else if (questionNumber >= 5 && questionNumber <= 8) {
            return score5to8;
        } else if (questionNumber >= 9 && questionNumber <= 10) {
            return score9to10;
        }
        return 0;
    }
}
