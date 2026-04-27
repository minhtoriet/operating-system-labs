import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class assignment {
    public static void main(String[] args) throws IOException, Exception {
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
                for (Task task : taskList) {
                    System.out.println(task.toString()); 
                }
                line = reader.readLine();
            } 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }

    }
}