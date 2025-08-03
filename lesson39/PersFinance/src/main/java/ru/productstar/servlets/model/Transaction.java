package ru.productstar.servlets.model;

public class Transaction {
    private final String name;
    private final int sum;
    private final boolean isIncome; // true для дохода, false для расхода

    public Transaction(String name, int sum, boolean isIncome) {
        this.name = name;
        this.sum = sum;
        this.isIncome = isIncome;
    }

    public String getName() {
        return name;
    }

    public int getSum() {
        return sum;
    }

    public boolean isIncome() {
        return isIncome;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "name='" + name + '\'' +
                ", sum=" + sum +
                ", isIncome=" + isIncome +
                '}';
    }
}
