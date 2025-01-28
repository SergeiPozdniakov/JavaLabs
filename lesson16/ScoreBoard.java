package lesson16;

import java.util.List;

public interface ScoreBoard {

    void addStudent(String name, Float score);

    List<String> top3();
}
