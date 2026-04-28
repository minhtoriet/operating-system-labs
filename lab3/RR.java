import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;

public class RR implements IScheduler {
    private CPU cpu;
    private int quantum = 100;
    public RR(CPU cpu){this.cpu = cpu;}

    @Override
    public List<String> Schedule(List<Task> taskList) {
        List<String> output = new ArrayList<>();
        ArrayDeque<Task> taskQueue = new ArrayDeque<Task>();
        for (Task task : taskList) {
            taskQueue.add(task);
        }
        int timepoint = 0;
        String cache;
        while (!taskQueue.isEmpty()){
            cache = String.valueOf(timepoint);
            int duration = cpu.execute(taskQueue.peek(), quantum);
            cache += taskQueue.peek().toString();
            output.add(cache);
            timepoint += duration;
            if (taskQueue.peek().getRemain() > 0){
                taskQueue.addLast(taskQueue.removeFirst());
            } else {
                taskQueue.remove();
            }
            //idx += (idx + 1) % taskList.size();
        }
        return output;
        
    }
}
