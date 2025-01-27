package org.example.students;

import org.example.students.exception.ItemNotFoundException;

import java.util.Collection;
import java.util.Set;

public interface Examination {

    String generateKey(String name, String subject);

    void addScore(Score score);

    Score getScore(String name, String subject) throws ItemNotFoundException;

    double getAverageForSubject(String subject);

    Set<String> multipleSubmissionsStudentNames();

    Set<String> lastFiveStudentsWithExcellentMarkOnAnySubject();

    Collection<Score> getAllScores();
}

