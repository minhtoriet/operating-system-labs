package lab4.assignment2;

import java.util.concurrent.Semaphore;

public class a2_semaphore {
    static int a = 0;
    static Semaphore s = new Semaphore(1);
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    try {
                        s.acquire();
                        a++;
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } finally {
                        s.release();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    try {
                        s.acquire();
                        a--;
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } finally {
                        s.release();
                    }
                }
            }
        });
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie){ie.printStackTrace();}
        System.out.println(a);
    } 
}
