package com.example.competition;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResultsProcessorTest {
    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = Files.createTempFile("results", ".csv");
        Files.write(testFile, List.of(
                "Иван Иванов, М, 10 км, 55:20",
                "Мария Петрова, Ж, 10 км, 50:30",
                "Алексей Сидоров, М, 5 км, 25:15",
                "Елена Кузнецова, Ж, 5 км, 28:40",
                "Сергей Васильев, М, 10 км, 52:10"
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.delete(testFile);
    }

    @Test
    void loadResults_parsesTimeCorrectly() throws IOException {
        ResultsProcessor processor = new ResultsProcessor();
        processor.loadResults(testFile.toString());

        List<Athlete> runners = processor.getTopNRunners(1, "5 км", "М");
        assertEquals(1515, runners.get(0).getTimeInSeconds()); // 25*60 + 15
    }

    @Test
    void getTopNRunners_returnsCorrectFor10kmMen() throws IOException {
        ResultsProcessor processor = new ResultsProcessor();
        processor.loadResults(testFile.toString());

        List<Athlete> top = processor.getTopNRunners(2, "10 км", "М");
        assertEquals(2, top.size());
        assertEquals("Сергей Васильев", top.get(0).getFullName());
        assertEquals("Иван Иванов", top.get(1).getFullName());
    }

    @Test
    void getTopNRunners_returnsCorrectFor5kmWomen() throws IOException {
        ResultsProcessor processor = new ResultsProcessor();
        processor.loadResults(testFile.toString());

        List<Athlete> top = processor.getTopNRunners(1, "5 км", "Ж");
        assertEquals(1, top.size());
        assertEquals("Елена Кузнецова", top.get(0).getFullName());
    }

    @Test
    void getTopNRunners_handlesNExceedingAvailable() throws IOException {
        ResultsProcessor processor = new ResultsProcessor();
        processor.loadResults(testFile.toString());

        List<Athlete> top = processor.getTopNRunners(10, "10 км", "М");
        assertEquals(2, top.size());
    }

    @Test
    void loadResults_throwsExceptionOnInvalidTime() {
        Path invalidFile = null;
        try {
            invalidFile = Files.createTempFile("invalid", ".csv");
            Files.write(invalidFile, List.of("Invalid, M, 10 км, 55:20:10"));

            ResultsProcessor processor = new ResultsProcessor();
            Path finalInvalidFile = invalidFile;
            assertThrows(IllegalArgumentException.class,
                    () -> processor.loadResults(finalInvalidFile.toString()));
        } catch (IOException e) {
            fail("Failed to create test file");
        } finally {
            if (invalidFile != null) {
                try {
                    Files.delete(invalidFile);
                } catch (IOException ignored) {}
            }
        }
    }
}
