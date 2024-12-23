package lesson8;

public class Circle {
         double radius = 0;

    public Circle (double radius) {
        this.radius = radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return Math.PI * 2 * getRadius();
    }
}
