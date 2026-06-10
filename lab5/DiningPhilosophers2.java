package lab5;
import java.util.concurrent.locks.*;

public class DiningPhilosophers2 {
    protected int chopsticks = 5;
    private Lock lock = new ReentrantLock();
    Thread[] philosophers = new Thread[5];
    protected final Condition grabable = lock.newCondition();
    boolean[] hasTwoForks = new boolean[5];
    public DiningPhilosophers2 (int n){
        this.chopsticks = n;
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philisopher(this,i);
        }
    }
    public Lock getLock(){return this.lock;}
    public Thread[] getPhilosophers() {return this.philosophers;}
    public void shutdown(){
        for (Thread p : philosophers) {
            p.interrupt();
        }
        Thread.currentThread().interrupt();
    }
}
class Philisopher extends Thread{
    int id;
    DiningPhilosophers2 dp2;
    public Philisopher(DiningPhilosophers2 dp2, int id){
        this.dp2 = dp2;
        this.id = id;
    }
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            this.think();
            while (dp2.chopsticks < 1) {
                try{
                    dp2.grabable.await();
                } catch (InterruptedException ie) {ie.printStackTrace();}
            }
            if (dp2.chopsticks >= 2)
            {
                dp2.getLock().lock();
                 try{
                    dp2.chopsticks--;
                } finally {
                    dp2.getLock().unlock();
                }
            }
            else if (dp2.chopsticks == 1){
                boolean canGrab = false;
                for (boolean check : dp2.hasTwoForks) {
                    if (check) {
                        canGrab = true;
                        break;
                    }
                }
                if (canGrab) {
                    dp2.getLock().lock();
                    try{
                        dp2.chopsticks--;
                    } finally {
                        dp2.getLock().unlock();
                    }
                }
            }
            while (dp2.chopsticks < 1) {
                try{
                    dp2.grabable.await();
                } catch (InterruptedException ie) {ie.printStackTrace();}
            }
            dp2.getLock().lock();
            try{
                dp2.chopsticks--;
                dp2.hasTwoForks[id] = true;
            } finally {
                dp2.getLock().unlock();
            }
            eat();
            //realease 2 chopsticks
            dp2.getLock().lock();
            try{
                dp2.chopsticks++;
                dp2.grabable.signalAll();
            } finally {
                dp2.getLock().unlock();
            }
            dp2.getLock().lock();
            try{
                dp2.chopsticks++;
                dp2.grabable.signalAll();
            } finally {
                dp2.getLock().unlock();
            }
        }
    }
    public void think(){
        try{
            Thread.sleep(100 + (int)(300*Math.random()));
            System.out.println("philosopher "+id+" is hungry");
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    public void eat(){
        try{
            Thread.sleep(50 + (int)(150*Math.random()));
            System.out.println("philosopher "+id+" ate");

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
class Main {
    public static void main(String[] args) {
        DiningPhilosophers2 dp = new DiningPhilosophers2(5);
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
