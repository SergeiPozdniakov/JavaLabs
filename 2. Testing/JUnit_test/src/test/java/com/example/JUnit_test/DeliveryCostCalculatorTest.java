package com.example.JUnit_test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для расчета стоимости доставки")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeliveryCostCalculatorTest {

    private DeliveryCostCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DeliveryCostCalculator();
    }

    @Test
    @Tag("basic")
    @DisplayName("Стандартный случай: 5 км, маленькие габариты, нехрупкий, normal")
    void testStandardCase() {
        double cost = calculator.calculateDeliveryCost(5, "small", false, "normal");
        assertEquals(400, cost, "Базовая стоимость 150, но минимальная сумма 400");
    }

    @Test
    @Tag("distance")
    @DisplayName("Расстояние 31 км, большие габариты, нехрупкий, normal")
    void testDistanceOver30() {
        double cost = calculator.calculateDeliveryCost(31, "large", false, "normal");
        assertEquals(500, cost, "300 (расстояние) + 200 (габариты) = 500");
    }

    @Test
    @Tag("fragile")
    @DisplayName("Хрупкий груз на расстоянии 29 км")
    void testFragileWithinRange() {
        double cost = calculator.calculateDeliveryCost(29, "large", true, "normal");
        assertEquals(700, cost, "200 (расстояние) + 200 (габариты) + 300 (хрупкость) = 700, но 700 * 1 = 700? Нет: 200+200+300=700");
        // 700 > 400, так что 700
    }

    @Test
    @Tag("fragile")
    @DisplayName("Хрупкий груз на расстоянии >30 км")
    void testFragileOver30() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDeliveryCost(31, "large", true, "normal");
        }, "Хрупкие грузы нельзя перевозить на расстояние более 30 км");
    }

    @Test
    @Tag("size")
    @DisplayName("Неверный тип габаритов")
    void testInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDeliveryCost(5, "medium", false, "normal");
        }, "Должна быть ошибка для неверного типа габаритов");
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'small', false, 'normal', 400",
            "2, 'small', false, 'normal', 400",
            "2.1, 'small', false, 'normal', 400",
            "10, 'small', false, 'normal', 400",
            "10.1, 'small', false, 'normal', 400",
            "30, 'small', false, 'normal', 400",
            "30.1, 'small', false, 'normal', 400",
            "31, 'small', false, 'normal', 400",
            "31, 'large', false, 'normal', 500",
            "15, 'large', false, 'normal', 400",
            "15, 'small', false, 'normal', 400",
            "3, 'large', false, 'normal', 400",
            "3, 'small', false, 'normal', 400",
            "1, 'small', false, 'normal', 400",
            "28, 'large', true, 'normal', 700",
            "29, 'large', true, 'normal', 700"
    })
    @Tag("parametrized")
    @DisplayName("Параметризованные тесты для разных сценариев")
    void testMultipleScenarios(double distance, String size, boolean fragile, String loadLevel, double expected) {
        double cost = calculator.calculateDeliveryCost(distance, size, fragile, loadLevel);
        assertEquals(expected, cost, "Расчет стоимости для " + distance + " км, " + size + ", fragile=" + fragile);
    }

    @ParameterizedTest
    @CsvSource({
            "31, 'large', false, 'very_high', 800",
            "31, 'large', false, 'high', 700",
            "31, 'large', false, 'elevated', 600",
            "31, 'large', false, 'normal', 500",
            "31, 'large', false, 'unknown', 500",
            "15, 'small', false, 'very_high', 480",
            "15, 'small', false, 'high', 420",
            "15, 'small', false, 'elevated', 400",
            "15, 'small', false, 'normal', 400",
            "15, 'small', false, 'unknown', 400"
    })
    @Tag("load_level")
    @DisplayName("Тесты для разных уровней загруженности")
    void testLoadLevelCoefficients(double distance, String size, boolean fragile, String loadLevel, double expected) {
        double cost = calculator.calculateDeliveryCost(distance, size, fragile, loadLevel);
        assertEquals(expected, cost, "Коэффициент загруженности для " + loadLevel);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 1.0, 2.0})
    @Tag("distance_boundaries")
    @DisplayName("Граничные значения расстояния (<=2 км)")
    void testDistanceLessOrEqual2(double distance) {
        double cost = calculator.calculateDeliveryCost(distance, "small", false, "normal");
        assertEquals(400, cost, "Базовая стоимость 150, но минимальная сумма 400");
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.1, 3, 9.9, 10})
    @Tag("distance_boundaries")
    @DisplayName("Граничные значения расстояния (2 < x <= 10 км)")
    void testDistanceBetween2And10(double distance) {
        double cost = calculator.calculateDeliveryCost(distance, "small", false, "normal");
        assertEquals(400, cost, "Базовая стоимость 200, но минимальная сумма 400");
    }

    @ParameterizedTest
    @ValueSource(doubles = {10.1, 11, 29.9, 30})
    @Tag("distance_boundaries")
    @DisplayName("Граничные значения расстояния (10 < x <= 30 км)")
    void testDistanceBetween10And30(double distance) {
        double cost = calculator.calculateDeliveryCost(distance, "small", false, "normal");
        assertEquals(400, cost, "Базовая стоимость 300, но минимальная сумма 400");
    }

    @ParameterizedTest
    @ValueSource(doubles = {30.1, 31, 35, 40})
    @Tag("distance_boundaries")
    @DisplayName("Граничные значения расстояния (>30 км)")
    void testDistanceOver30(double distance) {
        double cost = calculator.calculateDeliveryCost(distance, "small", false, "normal");
        assertEquals(400, cost, "Базовая стоимость 300+100=400");
    }

    @ParameterizedTest
    @ValueSource(doubles = {30.1, 31, 35, 40})
    @Tag("distance_boundaries")
    @DisplayName("Граничные значения расстояния (>30 км) с большими габаритами")
    void testDistanceOver30Large(double distance) {
        double cost = calculator.calculateDeliveryCost(distance, "large", false, "normal");
        assertEquals(500, cost, "Базовая стоимость 300+200=500");
    }

    @Nested
    @DisplayName("Тесты для минимальной суммы доставки")
    class MinimumCostTests {
        @Test
        void testMinimumCostForSmallDistance() {
            double cost = calculator.calculateDeliveryCost(1, "small", false, "normal");
            assertEquals(400, cost, "Базовая стоимость 150, но минимальная сумма 400");
        }

        @Test
        void testMinimumCostForSmallDistanceWithHighLoad() {
            double cost = calculator.calculateDeliveryCost(1, "small", false, "very_high");
            assertEquals(400, cost, "150 * 1.6 = 240, но минимальная сумма 400");
        }

        @Test
        void testMinimumCostForMediumDistance() {
            double cost = calculator.calculateDeliveryCost(5, "small", false, "normal");
            assertEquals(400, cost, "Базовая стоимость 150, но минимальная сумма 400");
        }

        @Test
        void testMinimumCostForMediumDistanceWithHighLoad() {
            double cost = calculator.calculateDeliveryCost(5, "small", false, "very_high");
            assertEquals(400, cost, "150 * 1.6 = 240, но минимальная сумма 400");
        }

        @Test
        void testMinimumCostForLargeDistance() {
            double cost = calculator.calculateDeliveryCost(31, "small", false, "normal");
            assertEquals(400, cost, "Базовая стоимость 400");
        }

        @Test
        void testMinimumCostForLargeDistanceWithHighLoad() {
            double cost = calculator.calculateDeliveryCost(31, "small", false, "very_high");
            assertEquals(640, cost, "400 * 1.6 = 640");
        }
    }
}
