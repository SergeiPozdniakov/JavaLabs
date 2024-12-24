package lesson8;

public class Circle {
        private double radius = 0;

    public Circle (double radius) {
        this.radius = radius;
    }

    void setRadius(double radius) {
        if (this.radius < 0) {
            throw new IllegalArgumentException("Радиус должен быть положительным");
        }
        this.radius = radius;
    }

    double getRadius() {
        return radius;
    }

    public double getArea() {
        return Math.PI * Math.pow(getRadius(), 2);
    }
}
