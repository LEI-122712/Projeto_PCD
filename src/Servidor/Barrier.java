package Servidor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    private final int participants;
    private int count;
    private int generation = 0; 
    private final Lock lock = new ReentrantLock();
    private final Condition barrierOpen = lock.newCondition();

    public Barrier(int participants) {
        this.participants = participants;
        this.count = 0;
    }

    public void await(long timeoutSeconds) throws InterruptedException {
        lock.lock();
        try {
            int myGeneration = generation; 
            count++;

            if (count == participants) {
                count = 0;
                generation++; 
                barrierOpen.signalAll();
            } else {
                long nanos = TimeUnit.SECONDS.toNanos(timeoutSeconds);
                while (count < participants && generation == myGeneration) {
                    if (nanos <= 0L) {
                        break; 
                    }
                    nanos = barrierOpen.awaitNanos(nanos);
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void reset() {
        lock.lock();
        try {
            generation++;    
            count = 0;        
            barrierOpen.signalAll(); 
        } finally {
            lock.unlock();
        }
    }
}