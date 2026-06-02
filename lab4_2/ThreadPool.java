package lab4_2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class ThreadPool {

}
class TSList<E> {
    private int size;
    private List<E> list;
    private Lock lock = new ReentrantLock();
    private Condition isAvailableCondition = lock.newCondition();
    public TSList(int size) {
        list = new ArrayList<>(size);
    }
    public void add (E item){
        lock.lock();
        try{
            
        } finally {lock.unlock();}
    }
}
class TSQueue<E> {
    private Queue<E> queue = new ArrayDeque<E>();
    private Lock lock = new ReentrantLock();
    private Condition notEmptyCondition = lock.newCondition();
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
}
