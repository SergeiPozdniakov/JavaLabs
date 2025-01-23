package lesson15;

/*
A nonempty array of integers of length n and the number k (0 < k <= n) is given.
It is necessary to output the average value for all subarrays of length k included in the original array.
*/

import java.util.ArrayList;
import java.util.List;

public class RollingAverage {

    public static List<Double> getRollingAverage(ArrayList<Integer> arr, int k) {
        List<Double> result = new ArrayList<>();
        int n = arr.size();
        double windowSum = 0;
        double res = 0;

        for (int i = 0; i < k; i++) {
            windowSum += arr.get(i);
        }
        res = Math.round((windowSum/k)*10.0)/10.0;
        result.add(res);

        for (int i = k; i < n; i++) {
            windowSum += arr.get(i) - arr.get(i - k);
            res = Math.round((windowSum/k)*10.0)/10.0;
            result.add(res);
        }

        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(12);
        arr.add(20);
        arr.add(30);
        arr.add(50);
        arr.add(-15);
        arr.add(25);
        arr.add(35);
        arr.add(55);

        int k = 3;
        List<Double> result = getRollingAverage(arr, k);

        System.out.println("Динамический исходный массив: " + arr);
        System.out.printf("Динамический массив средник значений: " + result);
    }
}