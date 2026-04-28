import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException, Exception{

        CPU Ryzen9_9950X3D = new CPU();
        List<Task> taskList = ReadFile();

        
        System.out.println("--==First come first serve==--");
        FCFS fcfs = new FCFS(Ryzen9_9950X3D);
        List<String> output = fcfs.Schedule(taskList);
        for (String string : output) {
            System.out.println(string);
        }

        taskList = ReadFile();
        System.out.println("\n--==Shortest job first==--");
        SJF sjf = new SJF(Ryzen9_9950X3D);
        output = sjf.Schedule(taskList);
        for (String string : output) {
            System.out.println(string);
        }

        taskList = ReadFile();
        System.out.println("\n--==Priority scheduling==--");
        Priority prio = new Priority(Ryzen9_9950X3D);
        output = prio.Schedule(taskList);
        for (String string : output) {
            System.out.println(string);
        }

        taskList = ReadFile();
        System.out.println("\n--==Round Robin==--");
        RR rr = new RR(Ryzen9_9950X3D);
        output = rr.Schedule(taskList);
        for (String string : output) {
            System.out.println(string);
        }

        taskList = ReadFile();
        System.out.println("\n--==Priority with Round Robin==--");
        RRPriority rrp = new RRPriority(Ryzen9_9950X3D);
        output = rrp.Schedule(taskList);
        for (String string : output) {
            System.out.println(string);
        }

    }



    static List<Task> ReadFile() throws IOException, Exception {
        List<Task> taskList = new ArrayList<>();
        String assignmentFileName = "schedule.txt";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(assignmentFileName));
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                String[] components = line.split(",");
                if (components.length < 3) throw new Exception("wrong input");
                for (int i = 0; i < components.length; i++) {
                    components[i] = components[i].trim();
                }
                taskList.add(new Task(components[0], Integer.parseInt(components[1]), Integer.parseInt(components[2])));
                // for (Task task : taskList) {
                //     System.out.println(task.toString()); 
                // }
                line = reader.readLine();
            } 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
        return taskList;
    }
}