package com.example.ege;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EgeResultsProcessorTest {

    @Autowired
    private EgeResultsProcessor processor;

    private Path keysFile;
    private Path answersFile;

    @BeforeEach
    void setUp() throws IOException {
        // Создаём временные файлы для тестов
        keysFile = Files.createTempFile("keys", ".txt");
        answersFile = Files.createTempFile("answers", ".txt");

        Files.write(keysFile, List.of(
                "1 - А",
                "2 - Б",
                "3 - В",
                "4 - Г",
                "5 - А",
                "6 - Б",
                "7 - В",
                "8 - Г",
                "9 - А",
                "10 - Б"
        ));

        Files.write(answersFile, List.of(
                "1 - А",  // ✅
                "2 - Б",  // ✅
                "3 - А",  // ❌
                "4 - Г",  // ✅
                "5 - А",  // ✅
                "6 - Г",  // ❌
                "7 - В",  // ✅
                "8 - Г",  // ✅
                "9 - А",  // ✅
                "10 - А"  // ❌
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(keysFile);
        Files.deleteIfExists(answersFile);
    }

    @Test
    void calculateTotalScore_returnsCorrectScore() throws IOException {
        int score = processor.calculateTotalScore(keysFile.toString(), answersFile.toString());

        // Правильные ответы: 1,2,4,5,7,8,9 → 7 вопросов
        // Баллы:
        // 1,2,4 → 1+1+1 = 3 (1-4)
        // 5,7,8 → 2+2+2 = 6 (5-8)
        // 9 → 3 (9-10)
        // Итого: 3 + 6 + 3 = 12

        assertEquals(12, score);
    }

    @Test
    void calculateTotalScore_handlesAllCorrect() throws IOException {
        // Делаем ответы одинаковыми
        Files.copy(keysFile, answersFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        int score = processor.calculateTotalScore(keysFile.toString(), answersFile.toString());

        // Все 10 правильных:
        // 1-4: 4 * 1 = 4
        // 5-8: 4 * 2 = 8
        // 9-10: 2 * 3 = 6
        // Итого: 18
        assertEquals(18, score);
    }

    @Test
    void calculateTotalScore_handlesAllWrong() throws IOException {
        Files.write(answersFile, List.of(
                "1 - Б",
                "2 - А",
                "3 - Г",
                "4 - В",
                "5 - Б",
                "6 - А",
                "7 - Г",
                "8 - В",
                "9 - Б",
                "10 - В"
        ));

        int score = processor.calculateTotalScore(keysFile.toString(), answersFile.toString());
        assertEquals(0, score);
    }

    @Test
    void loadAnswers_parsesFileCorrectly() throws IOException {
        Map<Integer, String> answers = processor.loadAnswers(keysFile.toString());
        assertEquals(10, answers.size());
        assertEquals("А", answers.get(1));
        assertEquals("Б", answers.get(2));
        assertEquals("Б", answers.get(10));
    }

    @Test
    void getScoreForQuestion_returnsCorrectScores() {
        assertEquals(1, processor.getScoreForQuestion(1));
        assertEquals(1, processor.getScoreForQuestion(4));
        assertEquals(2, processor.getScoreForQuestion(5));
        assertEquals(2, processor.getScoreForQuestion(8));
        assertEquals(3, processor.getScoreForQuestion(9));
        assertEquals(3, processor.getScoreForQuestion(10));
    }
}
