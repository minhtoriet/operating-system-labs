package semaphore;
// import java.util.concurrent.locks.Condition;
// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantLock;
public class MySemaphore {
    private int permit; 
    //private int blocking;
    // private Lock key = new ReentrantLock();
    // private Condition condition = this.key.newCondition();
    
    public MySemaphore (int permit){this.permit = permit;}

    public synchronized void acquire() throws InterruptedException{
        while (permit <= 0) wait();
        permit--;
    }
    public synchronized void release(){
       permit++;
       notifyAll();
    }
}

