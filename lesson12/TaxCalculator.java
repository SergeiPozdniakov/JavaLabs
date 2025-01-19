package lesson12;

import java.util.regex.Pattern;

public class TaxCalculator {
    private static final String[] prices = {"абвгд", "0", "-33.9", "777.9", "36.2", "100000000.0", "3720.9", "100"};
    private static final int TAX_10 = 10;
    private static final int TAX_20 = 20;
    private static final int TAX_30 = 30;
    private static final String PATTERN = "^[+]?[0-9]*\\.?[0-9]+(?:[eE][-+]?[0-9]+)?$";

    public static void main(String[] args) {
        printTax(prices);
    }

    private static void printTax(String[] prices) {
        for (String price : prices) {
            if (Pattern.matches(PATTERN, price)) {
                double value = Double.parseDouble(price);
                if (value > 0 && value <= 100) {
                    System.out.println("Ценник: " + price + ", Налог (10%): " + calcTax(value, TAX_10));
                } else if (value > 100 && value <= 1000) {
                    System.out.println("Ценник: " + price + ", Налог (20%): " + calcTax(value, TAX_20));
                } else if (value > 1000 && value <= 10000) {
                    System.out.println("Ценник: " + price + ", Налог (30%): " + calcTax(value, TAX_30));
                } else {
                    System.out.println("Ценник: " + price + " не обрабатывается (превышает 10000)");
                }
            } else {
                System.out.println("Некорректный ценник: " + price);
            }
        }
    }

    private static double calcTax(double value, int tax) {
        return value * tax / 100;
    }
}