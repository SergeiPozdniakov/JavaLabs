package lesson3;

import java.util.Arrays;

public class WorkShop {
    public static void main(String[] args) {
        Friend[] friends = {

                new Friend("Вася", 23, true, 6.0f),
                new Friend("Петя", 25, false, 8.5f),
                new Friend("Маша", 21, true, 7.0f),
                new Friend("Оля", 22, true, 5.5f)

        };
        System.out.println("My friends: " + Arrays.toString(friends));
    }
}

class Friend {
    private String name;
    private int age;
    private boolean friendship;
    private float hoursSpent;

    public Friend(String name, int age, boolean friendship, float hoursSpent) {
        this.name = name;
        this.age = age;
        this.friendship = friendship;
        this.hoursSpent = hoursSpent;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", friendship=" + friendship +
                ", hoursSpent=" + hoursSpent +
                '}';
    }
}