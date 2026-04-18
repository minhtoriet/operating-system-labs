package lab2;

import java.util.Scanner;

class assignment_2 {
    public static void main(String[] args) throws InterruptedException {
        Statistics s = new Statistics();
        Scanner sc = new Scanner(System.in);
        System.out.println("arr length: ");
        int a = sc.nextInt();
        int[] arr = new int[a];
        for (int i = 0; i < a; i++) {
            arr[i] = sc.nextInt();
        }

        AvgNumThread avg = new AvgNumThread(arr, s);
        MaxNumThread max = new MaxNumThread(arr, s);
        MinNumThread min = new MinNumThread(arr, s);

        avg.start();
        max.start();
        min.start();

        avg.join();
        max.join();
        min.join();

        System.out.println("the average value is " + (int) s.getAvg() + "\nThe min value is " + s.getMin()
                + "\nThe max value is " + s.getMax());
        sc.close();
    }
}

class AvgNumThread extends Thread {
    private int[] arr;
    private Statistics s;

    AvgNumThread(int[] arr, Statistics s) {
        this.arr = arr;
        this.s = s;
    }

    public void run() {
        double sum = 0;
        for (int i : arr) {
            sum += i;
        }
        s.setAvg(sum / arr.length);
    }
}

class MaxNumThread extends Thread {
    private int[] arr;
    private Statistics s;

    MaxNumThread(int[] arr, Statistics s) {
        this.arr = arr;
        this.s = s;
    }

    public void run() {
        int max = arr[0];
        for (int i : arr) {
            max = Math.max(max, i);
        }
        s.setMax(max);
    }
}

class MinNumThread extends Thread {
    private int[] arr;
    private Statistics s;

    MinNumThread(int[] arr, Statistics s) {
        this.arr = arr;
        this.s = s;
    }

    public void run() {
        int min = arr[0];
        for (int i : arr) {
            min = Math.min(i, min);
        }
        s.setMin(min);
    }
}

class Statistics {
    private int max;
    private int min;
    private double avg;

    Statistics() {
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }
}