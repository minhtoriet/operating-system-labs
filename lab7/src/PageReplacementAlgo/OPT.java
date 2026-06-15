package PageReplacementAlgo;

import java.util.LinkedList;
import java.util.List;

public class OPT {
    int[] pageRef;
    int frames;
    public OPT(int[] pageRef, int frames){
        this.pageRef = pageRef;
        this.frames = frames;
    }
    public int pageFaultsIncured(){
        int count = 0;
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < pageRef.length; i++) {
            if (list.contains(pageRef[i])) continue;
            if (list.size() == frames) {
                int max = 0;
                Integer large = null;
                for (Integer integer : list) {
                    int n = timeUnused(integer, i);
                    if (n >=  max) {
                        max = n;
                        large = integer;
                    }
                }
                list.remove((Integer) large);
            }
            list.add(pageRef[i]);
            count++;
        }
        return count;
    }
    private int timeUnused(int page, int currentIdx){
        int count = 1;
        for (int i = currentIdx + 1; i < pageRef.length; i++) {
            if (pageRef[i] != page) count++;
            else break;
        }
        return count;
    }
}
