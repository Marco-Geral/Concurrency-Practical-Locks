public class TTASLock {
    private volatile boolean locked = false;

    public void lock() {
        while (true) {
            if (!locked) {
                synchronized (this) {
                    if (!locked) {
                        //System.out.println("TTASLock acquired by thread: " + Thread.currentThread().getId());
                        locked = true;
                        return; // Lock acquired
                    }
                }
            } else {
                Thread.yield(); // Yield to reduce busy waiting
            }
        }
    }

    public synchronized void unlock() {
        //System.out.println("TTASLock released by thread: " + Thread.currentThread().getId());
        locked = false;
    }
}
