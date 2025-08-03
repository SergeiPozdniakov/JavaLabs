package ru.productstar.servlets.model;

public class Expense {
    private final String name;
    private final int sum;

    public Expense(String name, int sum) {
        this.name = name;
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", sum=" + sum +
                '}';
    }
}
