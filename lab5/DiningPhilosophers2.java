package lab5;
import java.util.concurrent.locks.*;

public class DiningPhilosophers2 {
    private int chopsticks;
    private Lock lock = new ReentrantLock();
    Thread[] philosophers = new Thread[5];
    public DiningPhilosophers2 (int n){
        this.chopsticks = n;
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philisopher(this);
        }
    }
    public int getChopsticks(){return this.chopsticks;}
    public Lock getLock(){return this.lock;}
}
class Philisopher extends Thread{
    DiningPhilosophers2 dp2;
    public Philisopher(DiningPhilosophers2 dp2){
        this.dp2 = dp2;
    }
    public void run(){
        
    }
}