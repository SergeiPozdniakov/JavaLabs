package lesson14;

import java.util.HashMap;
import java.util.Map;

/*Implement a method that swaps the keys and values in the HashMap. The HashMap<Integer, String> is received 
at the input to the method, it is necessary to return the HashMap<String, Integer>.
*/

public class HashMapTransfer {
	    public static void main(String[] args) {
	        // Создаем исходную HashMap
	        HashMap<Integer, String> inputMap = new HashMap<>();
	        inputMap.put(1, "One");
	        inputMap.put(2, "Two");
	        inputMap.put(3, "Three");

	        // Меняем ключи и значения местами
	        HashMap<String, Integer> outputMap = swapKeysAndValues(inputMap);

	        // Выводим результат
	        System.out.println("Original Map: " + inputMap);
	        System.out.println("Swapped Map: " + outputMap);
	    }

	    public static HashMap<String, Integer> swapKeysAndValues(HashMap<Integer, String> originalMap) {
	        HashMap<String, Integer> outputMap = new HashMap<>();

	        for (Map.Entry<Integer, String> entry : originalMap.entrySet()) {
	            outputMap.put(entry.getValue(), entry.getKey());
	        }

	        return outputMap;
	    }
	}


