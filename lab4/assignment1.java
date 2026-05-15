package lab4;

public class assignment1 {
    public static void main(String[] args) {
        PrintHE he = new PrintHE();
        PrintL l = new PrintL();
        PrintO o = new PrintO();

        he.start();
        l.start();
        o.start();

    }
    
}
class PrintHE extends Thread {
    public void run(){
        while (true){
            System.out.print("H");
            System.out.print("E");
        }
        
    }
}
class PrintL extends Thread{
    public void run(){
        while (true){
            System.out.print("L");
        }
    }
}
class PrintO extends Thread {
    public void run(){
        while (true){
            System.out.print("O");
        }
    }
}