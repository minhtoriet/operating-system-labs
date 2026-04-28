import java.util.ArrayList;
import java.util.List;

public class SJF implements IScheduler{
    CPU cpu;
    public SJF(CPU cpu){
        this.cpu = cpu;
    }

    @Override
    public List<String> Schedule(List<Task> taskList){
        List<String> output = new ArrayList<>();
        int timepoint = 0;
        String cache;
        taskList.sort((t1, t2) -> t1.getBurst() - t2.getBurst());
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
