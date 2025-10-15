package com.example;

// Вспомогательный класс "Заказ"
public class Order {
    private final String orderId;
    private final String userId;
    private final double amount;

    public Order(String orderId, String userId, double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }
    // Геттеры
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
}