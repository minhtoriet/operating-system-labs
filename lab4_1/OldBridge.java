package lab4_1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OldBridge {
    private int direction;
    private int carNum = 0;
    private final int maxCar = 3;
    private final Lock lock = new ReentrantLock();
    private final Condition allowedCond = lock.newCondition();
    public OldBridge(){}

    public void arriveBridge(int direction){
        lock.lock();
        try {
            while ((carNum != 0 && this.direction != direction) || (carNum == maxCar)) 
                allowedCond.await();
            this.direction = direction;
            carNum++;
            System.out.println("a car allowed on bridge. dir: "+direction+", car num: "+carNum);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } 
        finally {
            lock.unlock();
        } 
    }

    public void onBridge() {
        try {
            Thread.sleep(500 + (int)(500*Math.random()));
            exitBridge();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void exitBridge(){
        lock.lock();
        try{
            carNum--;
            allowedCond.signalAll();
            System.out.println("a car exited bridge. dir: "+direction+", car num: "+carNum);
        } finally {lock.unlock();}
    }
    public static void main(String[] args) {
        OldBridge ob = new OldBridge();
        Thread[] cars = new Thread[4];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Thread(new Runnable(){
                public void run(){
                    while (true){
                        try {
                            Thread.sleep(500*(int)(Math.random()));
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                        System.out.println("a car coming towards bridge");
                        ob.arriveBridge((int)Math.round(Math.random()));
                        ob.onBridge();
                    }
                }
            });
        }
        for (Thread thread : cars) {
            thread.start();
        }
        try {
            Thread.sleep(5000);
            for (Thread thread : cars) {
                thread.interrupt();
                thread.join();
            }
        }catch (InterruptedException ie) {ie.printStackTrace();}
        //Thread car1 = 
        

    }
}
