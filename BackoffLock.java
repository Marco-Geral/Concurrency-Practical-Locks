import java.util.Random;

public class BackoffLock {
    private volatile boolean locked = false;
    private final int minDelay, maxDelay;
    private final Random random;

    public BackoffLock(int minDelay, int maxDelay) {
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.random = new Random();
    }

    public void lock() {
        int delay = minDelay;
        while (true) {
            if (!locked) {
                synchronized (this) {
                    if (!locked) {
                        //System.out.println("BackoffLock acquired by thread: " + Thread.currentThread().getId());
                        locked = true;
                        return; // Lock acquired
                    }
                }
            } else {
                // Exponential backoff: simulate a delay
                int waitTime = random.nextInt(delay);
                for (int i = 0; i < waitTime; i++) {
                    Thread.yield(); // Yield to reduce contention
                }
                delay = Math.min(maxDelay, 2 * delay); // Exponential backoff
            }
        }
    }

    public synchronized void unlock() {
        //System.out.println("BackoffLock released by thread: " + Thread.currentThread().getId());
        locked = false;
    }
}
