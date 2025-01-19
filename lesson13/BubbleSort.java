package lesson13;

public class BubbleSort {
	public static void bubbleSort(int[] array, int size) {
	    for (int i=0; i < size - 1; i++) {
	    	for (int j=1; j < size - i; j++) {
	    		if (array[j-1] > array[j]) {
	    			int tmp = array[j];
	    			array[j] = array[j-1];
	    			array[j-1] = tmp;
	    		}
	    	}
	    } 
	}
	
}
