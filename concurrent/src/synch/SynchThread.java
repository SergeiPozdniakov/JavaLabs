package synch;

public class SynchThread extends Thread {
    private Thread thread;
    private final String threadName;
    final Counter counter;

    public SynchThread(String threadName, Counter counter) {
        this.threadName = threadName;
        this.counter = counter;
    }

    @Override
    public void run() {
        synchronized (counter) {
            System.out.println("Thread " + threadName + " is running...");
            counter.displayCounter(threadName);
            System.out.println("Leaving " + threadName + " thread...");
        }
    }

    public void start() {
        System.out.println("Thread " + threadName + " successfully started.");
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
