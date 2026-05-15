package semaphore;

public class Test {
    public static void main(String[] args) throws InterruptedException{
        //MySemaphore mys = null;
        MySemaphore mys = new MySemaphore(1);
        Plus p = new Plus(mys);
        Minus m = new Minus(mys);
        p.start();
        m.start();
        p.join();
        m.join();

        System.out.println(Data.a);
    }
    static class Data {
        static int a = 0;
    }
    static class Plus extends Thread{
        MySemaphore m;
        public Plus (MySemaphore m){this.m = m;}
        public void run(){
            for (int i = 0; i < 10000; i++) {
                try {
                    m.acquire();
                    Data.a++;
                } catch (InterruptedException ie) {ie.printStackTrace();}
                finally {m.release();}
            }
        }
    }
    static class Minus extends Thread{
        MySemaphore m;
        public Minus (MySemaphore m){this.m = m;}
        public void run(){
            for (int i = 0; i < 10000; i++) {
                try {
                    m.acquire();
                    Data.a--;
                } catch (InterruptedException ie) {ie.printStackTrace();}
                finally {m.release();}
            }
        }
    }
}
