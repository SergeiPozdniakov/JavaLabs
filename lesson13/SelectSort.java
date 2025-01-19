package lesson13;

public class SelectSort {
	public static void selectionSort(int[] array) {
	    for (int i=0; i < array.length - 1; i++) {
	    	int minElementIndex = i;
	    	for (int j = i + 1; j < array.length; j++) {
	    		if (array[minElementIndex] > array[j]) {
	    			minElementIndex = j;
	    		}
	    	}
	    	if (minElementIndex != i) {
	    		int temp = array[i];
	    		array[i] = array[minElementIndex];
	    		array[minElementIndex] = temp;
	    	}
	    }
    }

}
