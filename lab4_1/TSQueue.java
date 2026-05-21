package lab4_1;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TSQueue<E> {
    private Queue<E> queue = new ArrayDeque<E>();
    Lock lock = new ReentrantLock();
    Condition notEmptyCondition = lock.newCondition();
    public TSQueue(){}

    public void addLast (E item){
        lock.lock();
        try {
            queue.add(item);
            notEmptyCondition.signal();
        } 
        finally {lock.unlock();}
    }    

    public E removeFirst (){
        lock.lock();
        try {
            while (getSize() == 0) notEmptyCondition.await();
            return queue.poll();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return null;
        } 
        finally {lock.unlock();}
    }

    public int getSize (){
        lock.lock();
        try {
            return queue.size();
        } finally {lock.unlock();}
    }



    static TSQueue<Integer> tsqueue = new TSQueue<>();
    public static void main(String[] args) {
        
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    tsqueue.addLast(i);
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    tsqueue.removeFirst();
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie){ie.printStackTrace();}
        System.out.println(tsqueue.getSize());
    }
}
