package lab4;

import java.util.concurrent.Semaphore;

public class HELLO {
    public static void main(String[] args) {
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);
        Semaphore s3 = new Semaphore(0);
        PrintHE he = new PrintHE(s1,s2);
        PrintL l = new PrintL(s2,s3);
        PrintO o = new PrintO(s3,s1);

        he.start();
        l.start();
        o.start();
    }
    
}
class PrintHE extends Thread {
    Semaphore s1, s2;
    public PrintHE(Semaphore s1, Semaphore s2){
        this.s1 = s1;
        this.s2 = s2;
    }
    public void run() {
        while (true){
            try {
                s1.acquire();
                System.out.print("H");
                System.out.print("E");
                s2.release();
            } catch (InterruptedException ie){}  
        }
    }
}
class PrintL extends Thread{
    Semaphore s2, s3;
    int count = 0;
    public PrintL(Semaphore s2, Semaphore s3){
        this.s2 = s2;
        this.s3 = s3;
    }
    public void run(){
        while (true){
            try {                
                s2.acquire();
                System.out.print("L");
                count++;
                if (count == 2) {
                    count = 0;
                    s3.release();
                } else {
                    s2.release();
                }
            } catch (InterruptedException ie) {ie.printStackTrace();}
        }
    }
}
class PrintO extends Thread {
    Semaphore s3, s1;
    public PrintO(Semaphore s3, Semaphore s1){
        this.s3 = s3;
        this.s1 = s1;
    }
    public void run(){
        while (true){
             try {
                s3.acquire();
                System.out.print("O ");
                s1.release();
            } catch (InterruptedException ie) {ie.printStackTrace();}
        }
    }
}