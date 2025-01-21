package lesson14;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*Implement a method in which an ArrayList and a LinkedList are created and filled with 
1,000,000 random elements of the same type. After that, we select an element from a random 
index 1000 times from the ArrayList and LinkedList. Measure the time for ArrayList and LinkedList. 
Compare the results and suggest why they might be different.
*/

public class TestingLists {
    public static void main(String[] args) {
        // Размер списка
        int size = 1_000_000;
        // Количество выборок
        int iterations = 1000;

        // Создаем ArrayList
        List<Integer> arrayList = new ArrayList<>();
        fillList(arrayList, size);

        // Создаем LinkedList
        List<Integer> linkedList = new LinkedList<>();
        fillList(linkedList, size);

        long startTime = System.nanoTime();
        accessElements(arrayList, iterations);
        long arrayListTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        accessElements(linkedList, iterations);
        long linkedListTime = System.nanoTime() - startTime;

        System.out.println("ArrayList access time: " + arrayListTime + " ns");
        System.out.println("LinkedList access time: " + linkedListTime + " ns");
    }

    // Заполненяем список случайными числами
    private static void fillList(List<Integer> list, int size) {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
    }

    // Выбораем элементы по случайному индексу
    private static void accessElements(List<Integer> list, int iterations) {
        Random random = new Random();
        int size = list.size();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(size);
            list.get(index);
        }
    }
}
