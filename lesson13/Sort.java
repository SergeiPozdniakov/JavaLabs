package lesson13;

import java.util.Arrays;
import java.util.Random;

public class Sort {
	static int[] generateNumbers(int[] array, int size) {
		Random random = new Random();
		for (int i=0; i < size; i++) {
			array[i] = random.nextInt(10000);
		}
		return array;
	} 
	
	public static void printArray(int[] array) {
		for (int element: array) {
			System.out.print(element + " " );
		}
		System.out.println();
		System.out.println("--------------------------------");
	}
	
	static public void main(String[] args) {
		int size = 100000;
		int[] array = new int [size];
		//printArray(array);
		generateNumbers(array, size);
		//printArray(array);
		long startTime = System.currentTimeMillis();
	    BubbleSort.bubbleSort(array, size);
		long endTime = System.currentTimeMillis();
		System.out.println("Bubble Sort: " + (endTime - startTime));
		
		generateNumbers(array, size);
		startTime = System.currentTimeMillis();
		InsertionSort.insertionSort(array);
		endTime = System.currentTimeMillis();
		System.out.println("Insertion Sort " + (endTime - startTime));
		
		generateNumbers(array, size);
		startTime = System.currentTimeMillis();
		SelectSort.selectionSort(array);
		endTime = System.currentTimeMillis();
		System.out.println("Select Sort: " + (endTime - startTime));
		
		generateNumbers(array, size);
		startTime = System.currentTimeMillis();
		Arrays.sort(array);
		endTime = System.currentTimeMillis();
		System.out.println("Arrays.sort(): " + (endTime - startTime));
		
	}
}
