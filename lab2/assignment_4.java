package lab2;

import java.util.Random;

class assignment_4 {
    public static void main(String[] args) throws InvalidInputException, InvalidValueException, InterruptedException {
        if (args.length != 1)
            throw new InvalidInputException("Usage: input number of random points");
        if (args[0].compareTo("0") == 0)
            throw new InvalidValueException("Cannot estimate with 0 points");
        int input = Integer.parseInt(args[0]);
        Data data = new Data(input);
        Thread thread = new GeneratePointsThread(data);
        thread.start();
        thread.join();
        System.out.println("estimated pi: " + 
                            (4 * (double) data.getOutput() / (double) data.getInput()));
    }
}

class GeneratePointsThread extends Thread {
    Random r = new Random();
    private Data data;

    GeneratePointsThread(Data data) {
        this.data = data;
    }

    public void run() {
        int pointsInsideCircle = 0;
        for (int i = 0; i < data.getInput(); i++) {
            double x = generateRandomDouble(0.0, 1);
            double y = generateRandomDouble(0.0, 1);
            if (Double.compare(checkSquaredDistanceFromOrigin(x, y), 1) <= 0) {
                pointsInsideCircle++;
            }
        }
        data.setOutput(pointsInsideCircle);
    }

    double generateRandomDouble(double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

    double checkSquaredDistanceFromOrigin(double x, double y) {
        return (x * x + y * y);
    }

}

class Data {
    private int input;
    private int output;

    Data(int input) {
        this.input = input;
    }

     int getInput() {
        return input;
    }

    void setInput(int input) {
        this.input = input;
    }

    int getOutput() {
        return output;
    }

    void setOutput(int output) {
        this.output = output;
    }

}

