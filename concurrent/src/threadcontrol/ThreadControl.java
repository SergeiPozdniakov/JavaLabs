package threadcontrol;

public class ThreadControl implements Runnable {
    public Thread thread;
    private String threadName;
    boolean suspended = false;

    ThreadControl(String name) {
        threadName = name;
        System.out.println("Thread " + threadName + " successfully created.");
    }

    public void run() {
        System.out.println("Thread " + threadName + " is running...");
        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread " + threadName + " " + i);

                Thread.sleep(300);
                synchronized (this) {
                    while (suspended) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName + " is interrupted.");
        }
        System.out.println("Leaving thread " + threadName + "...");
    }

    public void start() {
        System.out.println("Starting " + threadName + " thread...");
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }
}
