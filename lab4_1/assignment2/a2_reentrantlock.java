package lab4_1.assignment2;

import java.util.concurrent.locks.*;

public class a2_reentrantlock {
    static int a = 0;
    static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    lock.lock();
                    a++;
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    lock.lock();
                    a--;
                    lock.unlock();
                }
            }
        });
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie){ie.printStackTrace();}
        System.out.println(a);
    }
}
