package lesson12;

import java.util.Scanner;
import static lesson12.Person.findPerson;
import static lesson12.Person.initBD;

public class PhoneBook {
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        initBD();

        System.out.print("Для поиcка по имени введите 1, по номеру 2, email 3: ");
        String searchType = in.nextLine();

        System.out.print("Введите поисковое значение: ");
        String searchString = in.nextLine();

        findPerson(searchType, searchString);

    }
}
