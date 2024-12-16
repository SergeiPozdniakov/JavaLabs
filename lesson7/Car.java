package lesson7;

public class Car {
    private String name;
    private int hp;
    private int weight;
    private Wheel wheel;
    private Engine engine;
    private Cartype cartype;

    public Car(String name, int hp, int weight) {
        this.name = name;
        this.hp = hp;
        this.weight = weight;
        this.wheel = new Wheel();
        this.engine = new Engine();

    }

    public class Wheel {
        public void steer() {
            System.out.println("Steer the wheel");
        }
    }

    public class Engine {
        public void startEngine() {
            System.out.println("Start engine");
        }
        public void stopEngine() {
            System.out.println("Stop engine");
        }
    }

    enum Cartype {
        SEDAN, WAGON, COUPE, SUV
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Wheel getWheel() {
        return wheel;
    }

    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Cartype getCartype() {
        return cartype;
    }

    public void setCartype(Cartype cartype) {
        this.cartype = cartype;
    }
}
