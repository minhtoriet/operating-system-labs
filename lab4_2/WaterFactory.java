package lab4_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

class WaterFactory {
    private int molecueCount;
    private int oxygenCount;
    private int hydrogenCount;
    private final Lock lock = new ReentrantLock();
    private final Condition enoughCond = lock.newCondition();
    public WaterFactory(){}
    public void hydrogen(){
        lock.lock();
        try {
            while (hydrogenCount == 2) enoughCond.await();
            hydrogenCount++;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        finally {lock.unlock();}
        generateWater();

    }
    public void oxygen(){
        lock.lock();
        try {
            while (oxygenCount == 1) enoughCond.await();
            oxygenCount++;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        finally {lock.unlock();}
        generateWater();
    }
    public void generateWater(){
        lock.lock();
        if (hydrogenCount == 2 && oxygenCount == 1) {
            molecueCount++;
            hydrogenCount = 0; oxygenCount = 0;
            System.out.println("1 water molecue created. total made: "+ molecueCount);
            enoughCond.signalAll();
        }
        lock.unlock();
    }
    public static void main(String[] args) {
        WaterFactory wf = new WaterFactory();
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
class HydrogenThread extends Thread {
    WaterFactory wf;
    HydrogenThread (WaterFactory wf) {this.wf = wf;}
    public void run(){
        try {
            Thread.sleep(100 + (int)(300*Math.random()));
        } catch (InterruptedException ie) {Thread.currentThread().interrupt();}
        System.out.println("1 hydrogen joined");
        wf.hydrogen();
    }
}
class OxygenThread extends Thread {
    WaterFactory wf;
    OxygenThread (WaterFactory wf) {this.wf = wf;}
    public void run(){
        try {
            Thread.sleep(100 + (int)(300*Math.random()));
        } catch (InterruptedException ie) {Thread.currentThread().interrupt();}
        System.out.println("1 oxygen joined");
        wf.oxygen();
    }
}
