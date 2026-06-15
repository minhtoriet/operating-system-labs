package lab5;

import java.util.ArrayList;

public class BankerAlgorithm {
    //private int resourceTypeNum;
    //private int customerNum;

    private int[] available;
    private int[][] maximum;
    private int[][] allocation;

    public BankerAlgorithm(int[] avail, int[][] max, int[][] alloc){
        available = avail;
        maximum = max;
        allocation = alloc;
    }
     
    public ArrayList<Integer> isSafeState() {
        ArrayList<Integer> result = new ArrayList<>();
        int[] work = available.clone();
        boolean[] finish = new boolean[allocation.length];
        for (boolean b : finish) {
            b = false;
        }

        boolean changed = true;
        //need = max - alloc
        do {
            changed = false;
            for (int j = 0; j < finish.length; j++) {
                boolean check = true;
                for (int k = 0; k < work.length; k++) {
                    if (maximum[j][k] - allocation[j][k] > work[k]){
                        check = false;
                        break;
                    }
                }
                if (finish[j] == false && check){
                    for (int i = 0; i < work.length; i++){
                        work[i] += allocation[j][i];
                    }
                    finish[j] = true;
                    changed = true;
                    result.add(j);
                }
            }
        } while (changed);
        
        if (!isAllTrue(finish)){
            return null;
        } else return result;

    }
    public boolean isAllTrue(boolean[] arr){
        for (boolean b : arr) {
            if (b == false) return false;
        }
        return true;
    }
    public ArrayList<Integer> request (int custId, int[] request) {
        for (int i = 0; i < request.length; i++) {
            if (request[i] > available[i]) return null; 
        }
        for (int i = 0; i < request.length; i++) {
            allocation[custId][i] += request[i];
            available[i] -= request[i];
        }
        return isSafeState();
    }
}
class Main {
    public static void main(String[] args) {
        int[][] alloc = {
            {0,1,0},
            {2,0,0},
            {3,0,2},
            {2,1,1},
            {0,0,2}
        };
        int[][] max = {
            {7,5,3},
            {3,2,2},
            {9,0,2},
            {2,2,2},
            {4,3,3}
        };
        int[] available = {3,3,2};
        BankerAlgorithm ba = new BankerAlgorithm(available, max, alloc);
        System.out.println(ba.isSafeState()); //1,3,4,0,2 can do as well as 1,3,4,2,0
        int[] request = {1,0,2};
        System.out.println(ba.request(1,request)); //should be satisfiable with 1,3,4,0,2
    }
}
