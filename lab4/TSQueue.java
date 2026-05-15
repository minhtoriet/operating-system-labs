package lab4;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class TSQueue<E> {
    private Queue<E> queue = new ArrayDeque<E>();
    Lock lock = new ReentrantLock();

    public TSQueue(){}

    

}
