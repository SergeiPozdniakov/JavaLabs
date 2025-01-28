package lesson16;

public class TestBoard {

    public static void main(String[] args) {
        ResultBoard board = new ResultBoard();
        board.addStudent("Гена", 4.34546f);
        board.addStudent("Гена", 4.9435634f);
        board.addStudent("Юля", 3.345634f);
        board.addStudent("Ирина", 5.0f);

        System.out.println(board.top3());
    }
}
