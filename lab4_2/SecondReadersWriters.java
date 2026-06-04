package lab4_2;

import java.util.concurrent.Semaphore;

public class SecondReadersWriters {
    private int data;
    private Semaphore readcount_mutex = new Semaphore(1);
    private Semaphore writecount_mutex = new Semaphore(1);
    private Semaphore readTry = new Semaphore(1);
    private Semaphore reader_mutex = new Semaphore(1);
    private Semaphore writer_mutex = new Semaphore(1);
    private int readCount = 0, writeCount = 0;
    public int read(){
        try {
            readTry.acquire();
            reader_mutex.acquire();
            readcount_mutex.acquire();
            try {
                readCount++;
                if (readCount == 1) {
                    writer_mutex.acquire();
                }
            } finally {
                reader_mutex.release();
                readTry.release();
            }
            int readValue = data;
            readcount_mutex.acquire();
            try {
                readCount--;
                if (readCount == 0) {
                    writer_mutex.release();
                }
            } finally {readcount_mutex.release();}
            return readValue;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("reader interrupted");
            return -1;
        }
    }
    public void write(int a){
        try {
            writecount_mutex.acquire();
            try {
                writeCount++;
                if (writeCount == 1) reader_mutex.acquire();
                
            } finally {
                writecount_mutex.release(); 
            }
            writecount_mutex.acquire();
            try{
                data = a;
            } finally {writer_mutex.release();}
            writecount_mutex.acquire();
            try {
                writeCount--;
                if (writeCount == 0) reader_mutex.release();
            } finally {writecount_mutex.release();}

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
