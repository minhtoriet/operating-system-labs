package lab4_1;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<E> {
    private Queue<E> queue = new ArrayDeque<E>();
    private final int maxSize;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmptyCondition = lock.newCondition();
    private final Condition notFullCondition = lock.newCondition();
    public BoundedBuffer(int size){
        this.maxSize = size;
    }

    public void add (E item){
        lock.lock();
        try {
            while (currentSize() == maxSize) notFullCondition.await();
            queue.add(item);
            notEmptyCondition.signal();
        } catch (InterruptedException ie){
            Thread.currentThread().interrupt();
        }
        finally {lock.unlock();}
    }    

    public E remove (){
        lock.lock();
        try {
            while (currentSize() == 0) notEmptyCondition.await();
            notFullCondition.signal();
            return queue.poll();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return null;
        } 
        finally {lock.unlock();}
    }

    public int currentSize (){
        lock.lock();
        try {
            return queue.size();
        } finally {lock.unlock();}
    }
    static BoundedBuffer<Integer> bb = new BoundedBuffer<>(15);
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    bb.add(i);
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    bb.remove();
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie){ie.printStackTrace();}
        System.out.println(bb.currentSize()); 

    }
}
