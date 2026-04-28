public class CPU {
    private Thread thr;
    CPU(){}
    
    public int execute(Task task){
        Runnable runnable = () ->{
            try{
                Thread.sleep(task.getRemain());
            } catch (InterruptedException e){
                this.interrupt();
            }
        };
        thr = new Thread(runnable);
        long start = System.currentTimeMillis();
        thr.start();
        try {
           thr.join();
        } catch (InterruptedException e){}
        long end = System.currentTimeMillis();
        int duration = (int)(end - start);
        task.setRemain(Math.max(task.getRemain() - duration, 0));
        thr = null;
        return duration;
    }
    public void interrupt() {
        if (thr != null){
            thr.interrupt();
        }
    }
    
    
    // for RR 
    public int execute(Task task, int quantum){
        Runnable runnable = () ->{
            try{
                Thread.sleep(task.getRemain());
            } catch (InterruptedException e){
                this.interrupt();
            }
        };
        thr = new Thread(runnable);
        long start = System.currentTimeMillis();
        thr.start();
        try {
           thr.join(quantum);
        } catch (InterruptedException e){}
        long end = System.currentTimeMillis();
        int duration = (int)(end - start);
        task.setRemain(Math.max(task.getRemain() - duration, 0));
        thr = null;
        return duration;
    }
}
