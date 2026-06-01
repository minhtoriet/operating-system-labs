package lab5;

import java.util.concurrent.locks.*;
//philosopher 1 gets fork 0 and 1, 4 gets 4 and 0
class DiningPhilosophers1 {
    private Thread[] philosophers = new Thread[5];
    private Lock[] locks = new Lock[5];
    public DiningPhilosophers1 (){
        for (int i = 0; i < philosophers.length; i++){
            locks[i] = new ReentrantLock();
            philosophers[i] = new Philisopher(i,locks);
        }
    }
    public void shutdown(){
        for (Thread p : philosophers) {
            p.interrupt();
        }
        Thread.currentThread().interrupt();
    }
    public Thread[] getPhilosophers() {
        return this.philosophers;
    }
}
class Philisopher extends Thread{
    private int id;
    private Lock[] locks = new Lock[5];
    public Philisopher (int id, Lock[] locks){
        this.id = id;
        this.locks = locks;
    }
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            this.think();
            if (id % 2 == 0){
                try {
                    locks[(id + 1) % 5].lock();
                    locks[id].lock();
                    System.out.println("philisopher "+id+" acquired two forks");
                    this.eat();
                } finally {
                    locks[(id + 1) % 5].unlock();
                    locks[id].unlock();
                    System.out.println("philisopher "+id+" released two forks");
                }
            }
            else {
                try {
                    locks[id].lock();
                    locks[(id + 1) % 5].lock();
                    System.out.println("philisopher "+id+" acquired two forks");
                    this.eat();
                } finally {
                    locks[(id + 1) % 5].unlock();
                    locks[id].unlock();
                    System.out.println("philisopher "+id+" released two forks");
                }
            }
        }
    }
    public void think(){
        try{
            Thread.sleep(100 + (int)(300*Math.random()));
            System.out.println("philosophopher "+id+" is hungry");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    public void eat(){
        try{
            Thread.sleep(50 + (int)(150*Math.random()));
            System.out.println("philisopher "+id+" ate");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
class Main {
    public static void main(String[] args) {
        DiningPhilosophers1 dp = new DiningPhilosophers1();
        Thread[] philisophers = dp.getPhilosophers();
        for (Thread thread : philisophers) {
            thread.start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {ie.printStackTrace();}
        dp.shutdown();
    }
}
