package org.example.students;

import org.example.students.exception.ItemNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ExaminationTest {

    private Examination examination;

    @BeforeEach
    void setUp() {
         examination = new ImplExamination(); { // examination это объект класса ImplExamination, который содержит в себе поле с HashMap
          }
        }

    @Test
    void testAddAndGet() throws ItemNotFoundException {

        Score score1 = new Score("Sergei Ivanov", "Math", 5);

        examination.addScore(score1);
        Score actual = examination.getScore("Sergei Ivanov", "Math");

        //System.out.println(actual);

        Assertions.assertEquals(score1, actual);
    }

    @Test
    void nullAdd() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> examination.getScore("Sergei Petrov", "Math"));
    }

    @Test
    void averageTest() {
        examination.addScore(new Score("Иван", "Математика", 5));
        examination.addScore(new Score("Иван", "Физика", 5)); // Иван сдал два экзамена
        examination.addScore(new Score("Мария", "Математика", 5)); // Мария сдала один экзамен
        examination.addScore(new Score("Петр", "Математика", 5));
        examination.addScore(new Score("Петр", "Химия", 4)); // Петр сдал два экзамена
        examination.addScore(new Score("Сеня", "Математика", 4)); // Петр сдал два экзамена
        examination.addScore(new Score("Сеня", "История", 5)); // Петр сдал два экзамена
        examination.addScore(new Score("Анна", "Математика", 4)); // Петр сдал два экзамена
        examination.addScore(new Score("Анна", "История", 5)); // Петр сдал два экзамена

        double averageMath = examination.getAverageForSubject("Математика");

        Assertions.assertEquals(4.5, averageMath);

        Set<String> assessmentFive = examination.lastFiveStudentsWithExcellentMarkOnAnySubject();
        System.out.println(assessmentFive);

        System.out.println(examination.getAllScores());
    }
}

