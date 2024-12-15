package lesson5;

import java.util.Arrays;

public class DynamicArray {
    static int size = 0;
    static int max_size = 2;
    static int[] data = new int[max_size];

    public static void main(String[] args) {
        print();
        System.out.println(isEmpty());
        add(100);
        add(35);
        add(29);
        add(1000);
        add(1);
        add(-3);
        print();
        System.out.println(isEmpty());
        remove(2);
        System.out.println("index = "+ indexOf(1));
        print();
        System.out.println(contains(3));
        sort();
        print();
    }

    static void print(){
        System.out.print("[");
        for(int i = 0; i < data.length; i++){
            System.out.print(" " + data[i]);
        }
        System.out.print(" ]");
        System.out.println();
    }

    static void add(int value) {
        if (size >= max_size) {
            int[] temp = Arrays.copyOf(data,size);
            max_size *= 2;
            data = new int[max_size];
            for (int i = 0; i < temp.length; i++){
                data[i] = temp[i];
            }
        }
        data[size] = value;
        size++;
    }

    static boolean isEmpty(){
        return size == 0;
    }

    static int indexOf(int value){
        for(int i = 0; i < data.length; i++){
            if (data[i] == value){
                return i;
            }
        }
        return -1;
    }

    static int get(int index){
        if(index < 0 || index > size){
            throw new RuntimeException("Ошибка");
        }
        return data[index];
    }

    static boolean contains(int value){
        return indexOf (value) >= 0;
    }

    static void remove(int index){
        if(index < 0 || index > size){
            throw new RuntimeException("Ошибка");
        }
        int[] temp = Arrays.copyOf(data, size);
        for(int i = index; i < temp.length - 1; i++){
            data[i] = temp[i + 1];
        }
        data[temp.length - 1] = 0;
       }

       static void sort() {
        if (size > 0) {
            for (int j = 0; j < data.length - 1; j++) {
                for (int i = 0; i < data.length - 1; i++) {
                    if (data[i] > data[i + 1]) {
                        int tmp = data[i + 1];
                        data[i + 1] = data[i];
                        data[i] = tmp;
                    }
                }
            }
        }
       }
}
