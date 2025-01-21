package lesson14;

import java.util.ArrayList;
import java.util.HashSet;

/* Implement a method that accepts an ArrayList of strings as input and removes all 
duplicates from it without using the contains() method. 
*/

public class RemoveSimilarObject {
    public static void main(String[] args) {
    	
        // Создаем ArrayList с повторяющимися элементами
        ArrayList<String> listSimilarObject = new ArrayList<>();
        listSimilarObject.add("Lada");
        listSimilarObject.add("Kalina");
        listSimilarObject.add("Largus");
        listSimilarObject.add("Lada");
        listSimilarObject.add("Largus");
        listSimilarObject.add("HAVAL");

        // Удаляем повторяющиеся элементы
        ArrayList<String> listWithoutDuplicates = removeSimilrities(listSimilarObject);

        // Выводим результат
        System.out.println("Original list: " + listSimilarObject);
        System.out.println("List without duplicates: " + listWithoutDuplicates);
    }

    public static ArrayList<String> removeSimilrities(ArrayList<String> list) {
        // Создаем HashSet
        HashSet<String> set = new HashSet<>(list);

        // Преобразуем HashSet обратно в ArrayList
        return new ArrayList<>(set);
    }
}
