public class TASLock {
    private volatile boolean locked = false;

    public void lock() {
        while (true) {
            synchronized (this) {
                if (!locked) {
                    locked = true;
                    //System.out.println("TASLock acquired by thread: " + Thread.currentThread().getId());
                    return;
                }
            }
            Thread.yield(); // Yield if lock isn't acquired
        }
    }

    public void unlock() {
        synchronized (this) {
            //System.out.println("TASLock released by thread: " + Thread.currentThread().getId());
            locked = false;
        }
    }
}
