package com.example.JUnit_test;

public class DeliveryCostCalculator {

    public double calculateDeliveryCost(double distance, String size, boolean fragile, String loadLevel) {
        // Проверка на хрупкость и расстояние > 30 км
        if (fragile && distance > 30) {
            throw new IllegalArgumentException("Хрупкие грузы нельзя перевозить на расстояние более 30 км");
        }

        double base = 0;

        // Расстояние
        if (distance > 30) {
            base += 300;
        } else if (distance > 10) {
            base += 200;
        } else if (distance > 2) {
            base += 100;
        } else {
            base += 50;
        }

        // Габариты
        if ("large".equals(size)) {
            base += 200;
        } else if ("small".equals(size)) {
            base += 100;
        } else {
            throw new IllegalArgumentException("Неверный тип габаритов: " + size + ". Допустимые значения: 'large', 'small'");
        }

        // Хрупкость
        if (fragile) {
            base += 300;
        }

        // Загруженность
        double coeff = 1.0;
        if ("very_high".equals(loadLevel)) {
            coeff = 1.6;
        } else if ("high".equals(loadLevel)) {
            coeff = 1.4;
        } else if ("elevated".equals(loadLevel)) {
            coeff = 1.2;
        }
        // Во всех остальных случаях коэффициент равен 1.0

        double total = base * coeff;

        // Минимальная сумма доставки
        return Math.max(total, 400);
    }
}