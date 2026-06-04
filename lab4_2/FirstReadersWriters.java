package lab4_2;

import java.util.concurrent.Semaphore;

public class FirstReadersWriters {
    private int data;
    private Semaphore resource = new Semaphore(1);
    private Semaphore rmutex = new Semaphore(1);
    private int readCount = 0;
    public int read(){
        try {
            rmutex.acquire();
            try {
                readCount++;
                if (readCount == 1) {
                    resource.acquire();
                }
            } finally {rmutex.release();}
            int readValue = data;
            rmutex.acquire();
            try {
                readCount--;
                if (readCount == 0) {
                    resource.release();
                }
            } finally {rmutex.release();}
            return readValue;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("reader interrupted");
            return -1;
        }
        
    }
    public void write(int a){
        try {
            resource.acquire();
            try {
                data = a;
            } finally {
                resource.release(); 
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.err.println("writer interrupted.");
        }
    }
}

class Reader {
    FirstReadersWriters frw;
    public Reader(FirstReadersWriters frw) {
        this.frw = frw;
    }
    public int readValue(){
        return frw.read();
    }
}
class Writer {
    FirstReadersWriters frw;
    public Writer(FirstReadersWriters frw) {
        this.frw = frw;
    }
    public void writeValue(int a){
        frw.write(a);
    }
}
