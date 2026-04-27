import java.util.ArrayList;

public class SJF implements Scheduler{
    public SJF(){}

    @Override
    public void Schedule(ArrayList<Task> taskList){
        taskList.sort((t1, t2) -> t1.getBurst() - t2.getBurst());
    }
}
