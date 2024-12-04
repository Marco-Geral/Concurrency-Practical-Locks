public class Main {
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        final TASLock tasLock1 = new TASLock();
        final TTASLock ttasLock1 = new TTASLock();
        final BackoffLock backoffLock1 = new BackoffLock(10, 1000);

        // Test the TASLock with simpler parameters
        System.out.println("Testing TASLock with 5 threads and 10000 increments");
        testLock(tasLock1, 5, 100000, "counter");

        // Test the TTASLock with simpler parameters
        System.out.println("Testing TTASLock with 5 threads and 10000 increments");
        testLock(ttasLock1, 5, 10000, "counter");

        // Test the BackoffLock with simpler parameters
        System.out.println("Testing BackoffLock with 5 threads and 10000 increments");
        testLock(backoffLock1, 5, 10000, "counter");
        System.out.println("\n");
//------------------------------------------------------------------------------------------------------------------------------        


        final TASLock tasLock2 = new TASLock();
        final TTASLock ttasLock2 = new TTASLock();
        final BackoffLock backoffLock2 = new BackoffLock(10, 1000);

        // Test the TASLock with simpler parameters
        System.out.println("Testing TASLock with 50 threads and 1000 increments");
        testLock(tasLock2, 50, 1000, "counter");

        // Test the TTASLock with simpler parameters
        System.out.println("Testing TTASLock with 50 threads and 1000 increments");
        testLock(ttasLock2, 50, 1000, "counter");

        // Test the BackoffLock with simpler parameters
        System.out.println("Testing BackoffLock with 50 threads and 1000 increments");
        testLock(backoffLock2, 50, 1000, "counter");
    }

    private static void testLock(Object lock, int numThreads, int numIncrements, String scenarioType) throws InterruptedException {
        counter = 0; // Reset counter before each test
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    // Acquire lock based on type
                    if (lock instanceof TASLock) {
                        ((TASLock) lock).lock();
                    } else if (lock instanceof TTASLock) {
                        ((TTASLock) lock).lock();
                    } else if (lock instanceof BackoffLock) {
                        ((BackoffLock) lock).lock();
                    }

                    // Critical section: increment shared counter
                    counter++;

                    // Release lock based on type
                    if (lock instanceof TASLock) {
                        ((TASLock) lock).unlock();
                    } else if (lock instanceof TTASLock) {
                        ((TTASLock) lock).unlock();
                    } else if (lock instanceof BackoffLock) {
                        ((BackoffLock) lock).unlock();
                    }
                }
            });
        }

        long startTime = System.currentTimeMillis();

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Lock: " + lock.getClass().getSimpleName() + ", Time: " + (endTime - startTime) + "ms, Final Counter: " + counter);
    }
}
