package lab4_1;

import java.util.concurrent.Semaphore;

class HOLE {
        public static void main(String[] args) {
            Semaphore s1 = new Semaphore(1);
            Semaphore s2 = new Semaphore(0);
            Semaphore s3 = new Semaphore(0);
            PrintHE he = new PrintHE(s1,s3);
            PrintL l = new PrintL(s2,s1);
            PrintO o = new PrintO(s3,s2);

            he.start();
            l.start();
            o.start();
        }
        
}
class PrintHE extends Thread {
    Semaphore s1, s3;
    public PrintHE(Semaphore s1, Semaphore s3){
        this.s1 = s1;
        this.s3 = s3;
    }
    public void run() {
        while (true){
            try {
                s1.acquire();
                System.out.print("H");
                s3.release();
                s1.acquire();
                
                System.out.print("E ");
                s1.release();
            } catch (InterruptedException ie){}  
        }
    }
}
class PrintL extends Thread{
    Semaphore s2, s1;
    int count = 0;
    public PrintL(Semaphore s2, Semaphore s1){
        this.s2 = s2;
        this.s1 = s1;
    }
    public void run(){
        while (true){
            try {                
                s2.acquire();
                System.out.print("L");
                s1.release();
            } catch (InterruptedException ie) {ie.printStackTrace();}
        }
    }
}
class PrintO extends Thread {
    Semaphore s3, s2;
    public PrintO(Semaphore s3, Semaphore s2){
        this.s3 = s3;
        this.s2 = s2;
    }
    public void run(){
        while (true){
             try {
                s3.acquire();
                System.out.print("O");
                s2.release();
            } catch (InterruptedException ie) {ie.printStackTrace();}
        }
    }
}
