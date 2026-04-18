package lab2;

class assignment_5 {
    public static void main(String[] args) throws InvalidInputException, InvalidValueException, InterruptedException {
        if (args.length != 1)
            throw new InvalidInputException("Usage: input number of fibonacci numbers");
        if (args[0].compareTo("0") == 0)
            throw new InvalidValueException("Cannot do with 0");
        int input = Integer.parseInt(args[0]);
        Data data = new Data(input);
        Thread fibonacciThread = new FibonacciGenerateThread(data);
        fibonacciThread.start();
        fibonacciThread.join();
        long[] arr = data.getFibonacciSequence();
        for (long i : arr) {
            System.out.println(i);
        }
    }
}

class FibonacciGenerateThread extends Thread {
    private Data data;

    FibonacciGenerateThread(Data data) {
        this.data = data;
    }

    public void run() {
        long[] arr = data.getFibonacciSequence();
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i < arr.length; i++) {
            arr[i] = arr[i - 2] + arr[i - 1];
        }
    }
}

class Data {
    private long[] fibonacciSequence;

    Data(int a) {
        fibonacciSequence = new long[a];
    }

    long[] getFibonacciSequence() {
        return fibonacciSequence;
    }

    void setFibonacciSequence(long[] fibonacciSequence) {
        this.fibonacciSequence = fibonacciSequence;
    }

}