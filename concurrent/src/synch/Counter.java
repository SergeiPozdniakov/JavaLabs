package synch;

public class Counter {
    public void displayCounter(String threadName){
        try {
            for(int i = 1; i<=5; i++){
                System.out.println("synch.Counter: " + i);
            }
        }catch (Exception e){
            System.out.println("Thread is interrupted.");
        }
    }
}