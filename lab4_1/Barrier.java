package lab4_1;

import java.util.concurrent.locks.*;

public class Barrier {
    private int parties = 0;
    private final int threshold;
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();
    private int gen = 0; 
    public Barrier (int threshold){
        this.threshold= threshold;
    }
    public void await(){
        try {
            int localGen = gen;
            lock.lockInterruptibly();
            parties++;
            if (parties == threshold) {
                parties = 0;
                gen++;
                cond.signalAll();
            } else {
                while (localGen == gen) cond.await();
            }
        } catch (InterruptedException ie){
            ie.printStackTrace();
        } finally {lock.unlock();}
    }

    public static void main(String[] args) {
        Barrier b = new Barrier(2);
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                System.out.println("thread 1 before awaiting");
                b.await();
                System.out.println("thread 1 freed");
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                System.out.println("thread 2 before awaiting");
                b.await();
                System.out.println("thread 2 freed");
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie){ie.printStackTrace();}
    }
}
