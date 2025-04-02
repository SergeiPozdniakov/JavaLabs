package synch;

public class SynchThreadDemo {
    public static void main(String[] args) {
        Counter counter = new Counter();

        SynchThread threadOne = new SynchThread("Synchronized Thread One", counter);
        SynchThread threadTwo = new SynchThread("Synchronized Thread Two", counter);

        threadOne.start();
        threadTwo.start();

        try {
            threadOne.join();
            threadTwo.join();
        }catch (InterruptedException e){
            System.out.println("Threads interrupted.");
            e.printStackTrace();
        }
    }
}
