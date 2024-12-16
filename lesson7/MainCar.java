package lesson7;

public class MainCar {

    public static void main() {


        Car mazda = new Car("Mazda", 120, 1800);
        Car lada = new Car("Largus", 85, 900);

        Electric BMW = new Electric("BMW", 200, 2000);

        testCar(mazda);
        testCar(lada);

        testCar(BMW);

    }
    public static void testCar(Car car) {
        System.out.println("Name car is " + car.getName() + " hp is " + car.getHp() + " weigh is " + car.getWeight());
        car.getEngine().startEngine();
        car.getWheel().steer();
        car.getEngine().stopEngine();
    }
}
