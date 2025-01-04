package lesson11;

public class StringPractice {
	public static void main(String[] args) {
        // Task 1
		String text = "В тексте, который вы видите на этом 12345 изображении, посчитайте количество букв 'е' в каждом слове напишите регулярное выражение для проверки телефона в международном формате с помощью регулярного выражения напишите функцию удаления всех букв и пробелов из текста";
        char character = 'е';
		
        calcChar(text, character);
        
        // Task 2
        regexMobile("+72342342364");
        regexMobile(text);
                
        // Task 3
        regexDel(text);
       
        // Workshop 
		/*len();
        split();
        trim();
        charAt();
        stringBuilder();
        checkRegex1();
        checkRegex2();
        checkRegex3();
        passwordRegex();*/
    }

	private static void calcChar (String input, char character) {
		String[] words = input.split(" ");
		
	    for (int i=0; i < words.length; i++) {
	    	int count=0;
	    	for (int j=0; j < words[i].length(); j++) {
				if (words[i].charAt(j) == character ) {
	    			count++;
	    		}
	    	}
	    	System.out.println("Слово " + (i+1) + ": " + count + " букв '" + character + "'");
	    }
	}
	
	public static void regexMobile(String mobile) {
		String regex = "^\\+[0-9]{1,3}[0-9]{7,14}$";
		boolean result = mobile.matches(regex);
		print(String.valueOf(result));
	}
	
	public static void regexDel(String input) {
		String regex = "[a-zA-Zа-яА-ЯёЁ\\s]";
		String result = input.replaceAll(regex, "");
		print(result);
	}
	
    /**
     * ^ - начало строки
     * [a-zA-Z]* - любое количество латинских букв
     * [0-9]+ - одна или более цифр
     * [a-zA-Z0-9]* - любое количество латинских букв и/или цифр
     * $ - конец строки
     */
    private static void passwordRegex() {
        String regex = "^[[a-zA-Z]+[0-9]*[a-zA-Z0-9]*]{5,8}$";
        String password = "qweйrty1";

        boolean result = password.matches(regex);
        print(String.valueOf(result));
    }

    private static void checkRegex3() {
        boolean res = "PrdStr".matches("[^a-z]?");
        print(String.valueOf(res));
    }

    private static void checkRegex2() {
        boolean res = "PrdStr".matches("[a-zA-Z]?");
        print(String.valueOf(res));
    }

    private static void checkRegex1() {
        boolean res = "q".matches("[a-z]");
        print(String.valueOf(res));
    }

    private static void charAt() {
        String str = "Строка";
        print(str.charAt(0) + "");
    }

    private static void stringBuilder() {
        int value1 = 300;
        double value2 = 3.14;
        short value3 = 5;
        char value4 = 'A';


        StringBuilder builder = new StringBuilder()
                .append(value1)
                .append("\n")
                .append(value2)
                .append("\n")
                .append(value3)
                .append("\n")
                .append(value4);

        String result = builder.toString();

        print(result);
    }

    private static void len() {
        String str = "Строка";
        print(str.length() + "");
    }

    private static void trim() {
        String str = "       Строка      ";
        print(String.valueOf(str.trim()));
    }

    private static void split() {
        String forSplit = "Нужно выделить отдельные слова";
        String[] res = forSplit.split(" ");
        for (String str : res) {
            print(str);
        }
    }

    private static void print(String text) {
        System.out.println(text);
    }
}


