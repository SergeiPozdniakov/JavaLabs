package lesson17;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/* Task 1. Write the deletion of all duplicate items from the list.
   Task 2. Write a count of the number of lines in the list that start with a certain letter.
   Task 3. Using the findFirst operator, write a search for the second largest element in the list of integers.
*/

public class StreamPractice {

    public static void main(String[] args) {
        uniqItems();
        calcStrings();
        findSecondItem();

    }
    // Task 1.
    public static void uniqItems() {
        List<String> list = Arrays.asList("apple", "peach", "pineapple", "apple");

        List<String> uniqList = list.stream().distinct().toList();

        System.out.println(list);
        System.out.println(uniqList);
    }
    // Task 2.
    public static void calcStrings() {

        List<String> list = Arrays.asList("apple", "peach", "pineapple", "apple");

        char letter = 'a';
        long count = list.stream().filter(s -> !s.isEmpty() && s.charAt(0) == letter).count();

        System.out.println("Количество строк, начинающихся на букву " + letter + " : " + count);
    }

    public static void findSecondItem() {

        List<Integer> list = Arrays.asList(10, 20, 30, 100, 10000);

        Optional<Integer> secondItem = list.stream().sorted((a, b) -> b.compareTo(a)).skip(1).findFirst();

        if (secondItem.isPresent()) {
            System.out.println("Второе максимальное значение = " + secondItem.get());
        }
        else {
            System.out.println("Исходный список чисел слишком мал");
        }
    }

}
