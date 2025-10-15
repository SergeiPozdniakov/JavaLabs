package com.example;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyWallet {
    private final String userId;
    private int balance;
    private final List<String> transactionHistory;

    public LoyaltyWallet(String userId) {
        this.userId = userId;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
        // Убираем автоматическую запись при создании, чтобы не мешала тестам
    }

    public void credit(int amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма к зачислению должна быть положительной");
        }
        this.balance += amount;
        transactionHistory.add("CREDIT: +" + amount + " баллов. Причина: " + reason + ". Баланс: " + balance);
    }

    public boolean debit(int amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма к списанию должна быть положительной");
        }
        if (amount > this.balance) {
            transactionHistory.add("FAILED DEBIT: Попытка списать " + amount + " баллов. Причина: " + reason + ". Недостаточно средств.");
            return false;
        }
        this.balance -= amount;
        transactionHistory.add("DEBIT: -" + amount + " баллов. Причина: " + reason + ". Баланс: " + balance);
        return true;
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public String getUserId() { return userId; }
    public int getBalance() { return balance; }
}