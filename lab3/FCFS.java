import java.util.ArrayList;
import java.util.List;

public class FCFS implements IScheduler{
    CPU cpu;
    public FCFS(CPU cpu){
        this.cpu = cpu;
    }

    @Override
    public List<String> Schedule(List<Task> taskList){
        List<String> output = new ArrayList<>();
        int timepoint = 0;
        String cache;
        for (Task task : taskList) {
            cache = String.valueOf(timepoint);
            int duration = cpu.execute(task);
            cache += task.toString();
            output.add(cache);
            timepoint += duration;
            cache = "";
        }
        return output;
    }
}
