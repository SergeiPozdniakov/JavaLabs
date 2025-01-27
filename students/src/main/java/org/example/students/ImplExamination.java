package org.example.students;

import org.example.students.exception.ItemNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/*
— Реализовать интерфейс, у которого есть методы.

[x] добавить сдачу студента (в зачет идет только последняя сдача, хранить все сдачи студента по одному и тому же предмету не нужно)
[x] получить сдачу студента по имени, фамилии и предмету
[x] вывод средней оценки по предмету вывод тех студентов, кто сдавал более одного раза
    - пройти по всем оценкам и посчитать, сколько экзаменов сдал каждый студент;
    - отфильтровать студентов, у которых количество сдач больше 1;
    - отфильтровать оценки по заданному предмету и по студентам, которые сдавали более 1 экзамена;
    - посчитать среднее значение.

[x] вывод последних пяти студентов, сдавших на отлично
[x] вывод всех сданных предметов

— Сделать кеш для вывода средней оценки по предмету за пределами интерфейса Examination.
*/
public class ImplExamination implements Examination {

    private static final int INITIAL_VOLUME = 256;

    public final Map<String, Score> items = new HashMap<>(INITIAL_VOLUME);

    private final List<Score> excellentScores = new ArrayList<>();


    @Override
    public String generateKey(String name, String subject) {
        return name + ":" + subject;
    }

    @Override
    public void addScore(Score score) {
        String key = generateKey(score.name(), score.subject());
        items.put(key, score);

        if (score.score() == 5) {
            excellentScores.add(score);
        }
    }

    @Override
    public Score getScore(String name, String subject) throws ItemNotFoundException {
        String key = generateKey(name, subject);
        Score score = items.get(key);
        if (score == null) {
            throw new ItemNotFoundException(name + " : " + subject);
        }
        return score;
    }

    @Override
    public double getAverageForSubject(String subject) {
        Set<String> studentsWithMultipleSubmissions = multipleSubmissionsStudentNames();
        int sum = 0;
        int count = 0;
        for (Score score : items.values()) {
            if (score.subject().equals(subject) && studentsWithMultipleSubmissions.contains(score.name())) {
                sum += score.score();
                count++;
            }
        }
        return count > 0 ? (double) sum/count : 0.0;
    }

    @Override
    public Set<String> multipleSubmissionsStudentNames() {
        Map<String, Integer> submissionCount = new HashMap<>();

        for (Score score : items.values()) {
            submissionCount.put(score.name(), submissionCount.getOrDefault(score.name(), 0) +1);
        }
        return submissionCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> lastFiveStudentsWithExcellentMarkOnAnySubject() {
        return excellentScores.stream()
                .sorted((s1, s2) -> s2.score() - s1.score())
                .limit(6) //
                .map(Score::name)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Score> getAllScores() {
        //return items.values();
        return new HashSet<>(items.values());
    }
}
