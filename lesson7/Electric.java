package lesson7;

public class Electric extends Car {

    public Electric(String name, int hp, int weight) {
        super(name, hp, weight);
    }

    @Override
    public Engine getEngine() {
        return new ElectricEngine();
    }
public class ElectricEngine extends Engine {
    @Override
    public void startEngine() {
        System.out.println("Turn on engine");
    }

    @Override
    public void stopEngine() {
        System.out.println("Turn off engine");
    }
   }
}
