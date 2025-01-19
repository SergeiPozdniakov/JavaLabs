package lesson13;

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
		int size = 10;
		int[] array = new int [size];
		printArray(array);
		generateNumbers(array, size);
		printArray(array);
	//	BubbleSort.bubbleSort(array, size);
	//	InsertionSort.insertionSort(array);
		SelectSort.selectionSort(array);
		printArray(array);
	}

}
