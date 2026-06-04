package lab4_2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class ThreadPool {
    private int size;
    AtomicInteger taskNum = new AtomicInteger(0);
    private TSList<Runnable> list;
    private TSQueue<Runnable> queue = new TSQueue<>();
    private final List<Worker> workers;
    public ThreadPool (int size) {
        this.size = size;
        list = new TSList<>(size);
        workers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public ThreadPool () {
        this(4);
    }

    public void add (Runnable item){
        taskNum.increment();
        if (list.currentSize() < size) list.add(item);
        else queue.addLast(item);
    }

    public void onCompletion(){
        if (taskNum.decrement() == 0) {
            System.out.println("test");
        }
    }

    private class Worker extends Thread{
        public void run(){
            while (true) {
                Runnable task = queue.removeFirst();
                if (task == null) break;
                try {
                    task.run();
                } catch (RuntimeException re) {re.printStackTrace();}
                finally {
                    onCompletion();
                }            
            }
        }
    }
}

class AtomicInteger {
    private int a;
    Lock lock = new ReentrantLock();
    public AtomicInteger (int a){
        this.a = a;
    }
    public int increment () {
        lock.lock();
        try {
            return ++a;
        } finally {
            lock.unlock();
        }
    }
    public int decrement () {
        lock.lock();
        try {
            return --a;
        } finally {
            lock.unlock();
        }
    }
}

class TSList<E> {
    private List<E> list;
    private Lock lock = new ReentrantLock();
    public TSList(int size) {
        list = new ArrayList<>(size);
    }
    public void add (E item){
        lock.lock();
        try{
            list.add(item);
        } finally {lock.unlock();}
    }
    public int currentSize() {
        return list.size();
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
            while (getSize() == 0) {
                try {
                    notEmptyCondition.await();
                } catch (InterruptedException ie) {
                    return null;
                }
            } 
            return queue.poll();
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
