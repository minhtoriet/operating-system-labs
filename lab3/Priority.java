import java.util.ArrayList;
public class Priority implements Scheduler {
    public void Schedule(ArrayList<Task> taskList){
        taskList.sort((t1, t2) -> t1.getPriority() - t2.getPriority());
    }
}   
