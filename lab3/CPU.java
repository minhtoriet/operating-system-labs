import java.util.ArrayList;
public class CPU {
    private Scheduler s;
    private ArrayList<Task> taskList;
    private int timeQuantum;
    CPU(Scheduler s){
        this.s = s;
        timeQuantum = s.getClass().getSimpleName() == "RR"? 10 : 0;
    }
    
    
    public CPU(ArrayList<Task> taskList){
        this.taskList = taskList;
    }
    public Scheduler getS() {
        return s;
    }
    public void setS(Scheduler s) {
        this.s = s;
    }
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
    

}
