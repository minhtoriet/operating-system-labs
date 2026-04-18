package lab2;

class assignment_1a {
    public static void main(String[] args) {
        Data number = new Data(0);
        Thread_1a_plus plus = new Thread_1a_plus(number, 1000000);
        Thread_1a_minus minus = new Thread_1a_minus(number, 1000000);
        System.out.println("initial num: " + number.getData());
        plus.start();
        try {
            plus.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("num after inc: " + number.getData());
        minus.start();
        try {
            minus.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("num after dec: " + number.getData());
    }
}

class Thread_1a_minus extends Thread {
    Data data;
    int repetition;

    Thread_1a_minus(Data data, int repetition) {
        this.data = data;
        this.repetition = repetition;
    }

    public void run() {
        for (int i = 0; i < repetition; i++) {
            int dec = data.getData() - 1;
            data.setData(dec);
        }
    }
}

class Thread_1a_plus extends Thread {
    Data data;
    int repetition;

    Thread_1a_plus(Data data, int repetition) {
        this.data = data;
        this.repetition = repetition;
    }

    public void run() {
        for (int i = 0; i < repetition; i++) {
            int inc = data.getData() + 1;
            data.setData(inc);
        }
    }
}

class Data {
    private int data;

    Data(int data) {
        this.data = data;
    }

    void setData(int aa) {
        this.data = aa;
    }

    int getData() {
        return data;
    }
}
