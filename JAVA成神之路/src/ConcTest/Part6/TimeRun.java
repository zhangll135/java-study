package ConcTest.Part6;

import java.util.concurrent.*;

import static ConcTest.Part5.Preload.launderThrowable;

public class TimeRun {
    private final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(2);
    public void timedRun(Runnable r, long timeout, TimeUnit unit){
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(()->taskThread.interrupt(),timeout,unit);
        r.run();
    }
}
class TimeRun2{
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(2);
    public static void timedRun(final Runnable r,long timeout,TimeUnit unit)throws InterruptedException{
        class RethrowableTask implements Runnable{
            private volatile Throwable t;
            public void run(){
                try{
                    r.run();
                }catch (Throwable t){
                    this.t=t;
                }
            }
            void rethrow(){
                if(t!=null)
                    throw launderThrowable(t);
            }
        }
        RethrowableTask task = new RethrowableTask();
        final Thread taskThread=new Thread(task);
        taskThread.start();
        cancelExec.schedule(()->taskThread.interrupt(),timeout,TimeUnit.MILLISECONDS);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }
}
class TimeRun3{
    private static final ExecutorService taskExec = Executors.newFixedThreadPool(2);
    public static void timedRun(final Runnable r,long timeout,TimeUnit unit)throws InterruptedException{
        Future<?> task = taskExec.submit(r);
        try{
            task.get(timeout,unit);
        }catch (TimeoutException e){
            //接下来任务取消
            System.out.println("time up!");
        }catch (ExecutionException e){
            //运行中抛出异常
            throw launderThrowable(e.getCause());
        }finally {
            //任务已结束，执行取消任务也不会有影响
            task.cancel(true);
        }
    }
    public static void shutDown(){
        System.out.println("线程池关闭！");
        taskExec.shutdown();
    }
}

