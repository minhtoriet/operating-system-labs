package SleepingBarber;
import java.util.concurrent.locks.*;

public class Test {
    private static final int MAX_CHAIR = 3;
    private Thread barberThread;
    private Thread customerThread;
    private boolean running = true;
    private int customerCount = 0;
    private final Lock lock = new ReentrantLock();
    private Condition notFullCondition = lock.newCondition();
    private Condition notEmptyCondition = lock.newCondition();

    public static void main(String[] args) {
        Test test = new Test();
        
        test.barberThread.start();
        test.customerThread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {ie.printStackTrace();}
        
        test.shutdown();
    
    }    
    public Test(){
        this.barberThread = new Thread(new Runnable() {
            public void run(){
                barberJob();
            } 
        });
        //this.barberThread = new Thread(this::barberJob);
        this.customerThread = new Thread(new Runnable (){
            public void run(){
                customerVisit();
            }
        });
    }
    private void barberJob (){
        while (this.running && !Thread.currentThread().isInterrupted()){
            lock.lock();
            try {
                while (customerCount == 0) notEmptyCondition.await();

                customerCount--;
                System.out.println("customer acquired. Total customer left: "+customerCount);
                notFullCondition.signal();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
            finally {
                lock.unlock();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie){
                Thread.currentThread().interrupt();
                break;
            }
            System.out.println("customer shaved");
        }
    }
    public void shutdown (){
        this.running = false;
        this.barberThread.interrupt();
        this.customerThread.interrupt();
        try{
            this.barberThread.join();
            this.customerThread.join();
        }catch (InterruptedException ie) {ie.printStackTrace();}
    }
    public void customerVisit(){
        while (this.running && !Thread.currentThread().isInterrupted()){
            lock.lock();
            try{
                while (customerCount == MAX_CHAIR) notFullCondition.await();
                
                customerCount++;
                notEmptyCondition.signal();
                System.out.println("1 customer joined the line. Total customer waiting: " +customerCount);
            } catch(InterruptedException ie){
                Thread.currentThread().interrupt();
                break;
            }
            finally {lock.unlock();}
            try {
                Thread.sleep((int)(1000 * Math.random()));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
