package ConcTest.Part6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NextTask {
    BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    public NextTask()throws InterruptedException{
        for (int i=0;i<10;i++)
        queue.put(i);
    }
    public Integer getNextTask(){
        boolean interrupted=false;
        try{
            while (true){
                try{
                    return queue.take();
                }catch (InterruptedException e){
                    interrupted=true;
                }
            }
        }finally {
            if(interrupted)
                Thread.currentThread().interrupt();
        }
    }
}
