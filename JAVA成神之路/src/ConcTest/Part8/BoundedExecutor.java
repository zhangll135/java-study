package ConcTest.Part8;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;
    public BoundedExecutor(Executor executor,int bound){
        this.exec = executor;
        this.semaphore = new Semaphore(bound);
    }
    public void submitTask(final Runnable command)throws InterruptedException{
        System.out.println("semaphone: "+semaphore.availablePermits());
        semaphore.acquire();
        try{
            exec.execute(()->{
                try{
                    command.run();
                }finally {
                    semaphore.release();
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }
    }
}
