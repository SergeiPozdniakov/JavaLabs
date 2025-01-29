package lesson17;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorkShop17 {

        public static void main(String[] args) {
            exc1();
            exc2();
            exc3();
            exc4();
            exc5();
        }

        private static void exc5() {
            // максимальное значение
            List<Integer> nums = Arrays.asList(1, 2, 3);

            Optional<Integer> max = nums.stream()
                    .max(Integer::compare);

            System.out.println(max.orElse(0));
        }

        private static void exc4() {
            // в верхний регистр
            List<String> colors = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");

            List<String> uppercaseStrings = colors.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            System.out.println(uppercaseStrings);
        }

        private static void exc3() {
            // вычислить среднее значение
            List<Integer> nums = Arrays.asList(1, 3, 6, 8, 10, 18, 36);

            double average = nums.stream()
                    .mapToDouble(Integer::doubleValue)
                    .average()
                    .orElse(0.0);

            System.out.println(average + "");
        }

        private static void exc1() {
            // cортировка строк в порядке возрастания
            List<String> colors = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
            System.out.println(colors);

            List<String> result = colors.stream()
                    .sorted()
                    .collect(Collectors.toList());

            System.out.println(result);
        }

        private static void exc2() {
            // сумма всех четных и нечетных чисел
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            int sumOfEvens = numbers.stream()
                    .filter(num -> num % 2 == 0)
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println(sumOfEvens + "");

            int sumOfOdds = numbers.stream()
                    .filter(num -> num % 2 != 0)
                    .mapToInt(Integer::intValue)
                    .sum();
            System.out.println(sumOfOdds + "");
        }


}
