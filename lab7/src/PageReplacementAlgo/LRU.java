package PageReplacementAlgo;

import java.util.LinkedList;

public class LRU {
    int[] pageRef;
    int frames;
    public LRU(int[] pageRef, int frames){
        this.pageRef = pageRef;
        this.frames = frames;
    }
    public int pageFaultsIncured(){
        int count = 0;
        LinkedList<Integer> list = new LinkedList<>();
        for (Integer integer : pageRef) {
            if (list.contains(integer)) {
                list.removeLastOccurrence(integer);
                list.addFirst(integer);
                continue;
            }
            if (list.size() == frames) {
                list.removeLast();
            }
            list.addFirst(integer);
            count++;
        }
        return count;
    }
}
