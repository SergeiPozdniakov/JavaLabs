package com.example.ege;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EgeApplication {

    // Путь к файлам
    private static final String KEYS_FILE_PATH = "src/main/resources/keys.txt";
    private static final String ANSWERS_FILE_PATH = "src/main/resources/answers_student.txt";

    public static void main(String[] args) {
        SpringApplication.run(EgeApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(EgeResultsProcessor resultsProcessor) {
        return args -> {
            try {
                int score = resultsProcessor.calculateTotalScore(
                        "keys.txt",
                        "answers_student.txt"
                );
                System.out.println("Final score for EGE: " + score);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
