package SleepingBarber;
import java.util.concurrent.locks.*;

public class Test {
    private static final int MAX_CHAIR = 3;
    private Thread barberThread;
    private boolean running = true;
    private int customerCount = 0;
    private final Lock lock = new ReentrantLock();
    private Condition notFullCondition = lock.newCondition();
    private Condition notEmptyCondition = lock.newCondition();
    public Test(){
        this.barberThread = new Thread(new Runnable() {
            public void run(){

            } 
        });
    }
    private void barberJob (){
        while (this.running){
            lock.lock();
            try {
                while (customerCount == 0) notEmptyCondition.await();
                /* */
                notFullCondition.signalAll();

            } catch (InterruptedException ie) {ie.printStackTrace();}
            finally {
                lock.unlock();
            }
        }
    }
    public int getCustomerCount(){
        return this.customerCount;
    }
    public void shutdown (){
        this.running = false;
        this.barberThread.interrupt();
        try{
            this.barberThread.join();
        }catch (InterruptedException ie) {ie.printStackTrace();}
    }
}
class Customer {

}