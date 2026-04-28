import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RRPriority implements IScheduler {
    private CPU cpu;
    private int quantum = 100;
    public RRPriority(CPU cpu){this.cpu = cpu;}

    @Override
    public List<String> Schedule(List<Task> taskList) {
        List<String> output = new ArrayList<>();
        Map<Integer, ArrayDeque<Task>> priorityMap = new HashMap<>();
        int timepoint = 0;

        for (Task task : taskList) {
            priorityMap.putIfAbsent(task.getPriority(), new ArrayDeque<>());
            priorityMap.get(task.getPriority()).add(task);
        }
        List<Integer> priorityList = new ArrayList<>(priorityMap.keySet());
        priorityList.sort((a, b) -> a - b);

        for (Integer integer : priorityList) {
            ArrayDeque<Task> taskQueue = priorityMap.get(integer);
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
        }
        return output;
        
    }

}
