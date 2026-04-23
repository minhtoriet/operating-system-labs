package lab3;

public class Task {
    private final String name;
    private final int priority;
    private final int burst;
    private int remain;
    Task(String n, int p, int b){
        name = n; priority = p; burst = b; remain = burst;
    }
    public void setRemain(int remain) {
        this.remain = remain;
    }
    public String getName() {
        return name;
    }
    public int getPriority() {
        return priority;
    }
    public int getBurst() {
        return burst;
    }
    public int getRemain() {
        return remain;
    }
    @Override
    public String toString() {
        return "Task [name=" + name + ", priority=" + priority + ", burst=" + burst + ", remain=" + remain + "]";
    }
    
}
