package lesson12;
 
import java.util.Scanner;

public class Calculator {

	public static void printArr(double[] array) {
		for (double element: array) {
			System.out.printf("%.5f%n", element);
		}
	}
	
	public static void main(String[] args) {
    	
    	
        int n = 2;
    	double[] result = new double[n];
    	
        for (int i=0; i <= result.length; i++) {
        	if (i == result.length) {
                System.out.println("\nМассив данных полный");
                System.out.println("\nВывод массива на печать:");
                printArr(result);
        		break;}
        System.out.println("\nВведите 2 числа или напишите \"выход\"");
        Scanner reader = new Scanner(System.in);
        String input = reader.next();
        if (input.equalsIgnoreCase("выход")) {
        	break;
        }
    	
        double first = Double.parseDouble(input);
        double second = reader.nextDouble();

        System.out.println("Введите оператор: (+, -, *, /): ");
        char operator = reader.next().charAt(0);

        double calcResult = 0.0;

        switch (operator) {
            case '+':
                calcResult = first + second;
                break;

            case '-':
            	calcResult = first - second;
                break;

            case '*':
            	calcResult = first * second;
                break;

            case '/':
            	calcResult = first / second;
                break;

            default:
                System.out.printf("Введите корректный оператор");
                continue;
        }
        result[i] = calcResult;
        System.out.printf("%.1f %c %.1f = %.1f", first, operator, second, calcResult);
      }
        

    }
}

/*
 *     ДЗ
 *     1. добавьте массив для сохранения результатов размерностью 10
 *     если результатов стало больше мы завершаем работы, информируя пользователя и распечатывая результаты
 *
 *     2. поместите код в цикл для возможности использования без постоянного запуска программы.
 *     Для выхода пусть буду слова "выход"
 *     T.е. пользователь ввел выход - мы просто выходим, сохраняя результат в массиве результатов и выводим массив на консоль.
 *
 *
 */
