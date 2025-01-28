package lesson16;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ResultBoard implements ScoreBoard {

    TreeSet<Student> board;

    public ResultBoard() {
        board = new TreeSet<>(Comparator
                .comparing(Student::getScore).reversed()
                .thenComparing(Student::getName));
    }

    @Override
    public void addStudent(String name, Float score) {
       board.add(new Student(name, score));
    }

    @Override
    public List<String> top3() {
        return board.stream().limit(3)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

private static class Student {

    String name;
    Float score;

    public Student(String name, Float score) {
    this.name = name;
    this.score = score;
    }

    public String getName() {
        return name;
    }

    public Float getScore() {
        return score;
    }
  }
}
