package lesson5;

public class Stack {
    static int max_size = 5;
    static int size = 0;
    static int[] data = new int[max_size];

    public static void main(String[] args) {
        System.out.println(isEmty());
        push(0);
        push(5);
        push(8);
        push(3);
        push(1);
        print();
        System.out.println(pop());
        System.out.println(pop());
        System.out.println(peek());
        print();

    }

    static void push(int value){
        if (size >= max_size) {
            throw new RuntimeException("FULL");
        }
        data[size] = value;
        size++;
    }

    static int pop(){
        if (size == 0) {
            throw new RuntimeException("EMPTY");
        }
        int result = data[size - 1];
        size--;
        return result;
    }

    static int peek() {
        if (size == 0) {
            throw new RuntimeException("Out of bound");
        }
        return data[size - 1];
    }

    static void print() {
        if (size > 0) {
            System.out.println("=============");
            for (int i = size - 1; i >= 0; i--) {

                System.out.println("| " + data[i] + " |");

            }
            System.out.println("=============");
        }
    }

    static boolean isEmty() {
        return size == 0;
    }
}
