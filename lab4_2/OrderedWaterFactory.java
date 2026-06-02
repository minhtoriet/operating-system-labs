package lab4_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderedWaterFactory implements IWaterFactory{
    private int state = 0;
    private int molecueCount;
    //private int oxygenCount;
    //private int hydrogenCount;
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    public OrderedWaterFactory(){}
    public void hydrogen(){
        lock.lock();
        try {
            while (state == 1) cond.await();
            //hydrogenCount++;
            System.out.println("1 hydrogen joined");
            if (state == 0) state = 1; else {
                molecueCount++;
                //hydrogenCount = 0; oxygenCount = 0;
                System.out.println("1 water molecue created. total made: "+ molecueCount);
                state = 0;
            }
            cond.signalAll();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        finally {lock.unlock();}
    }
    public void oxygen(){
        lock.lock();
        try {
            while (state != 1) cond.await();
            //oxygenCount++;
            System.out.println("1 oxygen joined");
            state = 2;
            cond.signalAll();
            
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        finally {lock.unlock();}
        generateWater();
    }
    public void generateWater(){
    }
    public static void main(String[] args) {
        OrderedWaterFactory wf = new OrderedWaterFactory();
        List<Thread> threadList = new ArrayList<>(); 
        for (int i = 0; i < 100; i++) {
            Thread t1 = new HydrogenThread(wf);
            Thread t2 = new HydrogenThread(wf);
            Thread t3 = new OxygenThread(wf);
            threadList.add(t1);
            threadList.add(t2);
            threadList.add(t3);
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException ie) {ie.printStackTrace();}
        }

    }
}
