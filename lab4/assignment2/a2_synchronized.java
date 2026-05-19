package lab4.assignment2;

public class a2_synchronized {
    static int a = 0;
    static final Object o = new Object();
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    synchronized(o){
                        a++;
                    }                
                }
            }
        });
        Thread t2 = new Thread(new Runnable(){
            public void run(){
                for (int i = 0; i < 1000000; i++) {
                    synchronized(o){
                        a--;
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
