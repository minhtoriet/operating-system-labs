package PageReplacementAlgo;

import java.util.ArrayDeque;
import java.util.Queue;

public class FIFO {
    int[] pageRef;
    int frames;
    public FIFO(int[] pageRef, int frames){
        this.pageRef = pageRef;
        this.frames = frames;
    }
    public int pageFaultsIncured(){
        int count = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        for (Integer integer : pageRef) {
            if (queue.contains(integer)) continue;
            if (queue.size() >= frames) {
                queue.poll();
            }
            queue.add(integer);
            count++;
        }
        return count;
    }
}