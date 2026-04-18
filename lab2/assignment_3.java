package lab2;

class assignment_3 {
    public static void main(String[] args) throws InterruptedException, InvalidInputException {
        if (args.length == 0 || args.length > 1)
            throw new InvalidInputException("usage: input an integer");
        int a = Integer.parseInt(args[0]);
        PrimeNumPrintThread primePrint = new PrimeNumPrintThread(a);
        primePrint.start();
        primePrint.join();

    }
}

class PrimeNumPrintThread extends Thread {
    private int a;

    PrimeNumPrintThread(int a) {
        this.a = a;
    }

    public void run() {
        for (int i = 2; i <= a; i++) {
            if (isPrime(i))
                System.out.println(i);
        }
    }

    static boolean isPrime(int a) {
        if (a < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(a); i++) {
            if (a % i == 0)
                return false;
        }
        return true;
    }
}
