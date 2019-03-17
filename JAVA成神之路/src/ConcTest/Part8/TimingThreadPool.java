package ConcTest.Part8;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TimingThreadPool extends ThreadPoolExecutor {
    public TimingThreadPool()throws IOException{
        this(100,1000);
    }
    public TimingThreadPool(int nThread,int nCapacity)throws IOException {
        super(nThread,nThread,0, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(nCapacity));
        log.setUseParentHandlers(false);
        FileHandler fh = new FileHandler("TimingThreadPool");
        log.addHandler(fh);
        fh.setFormatter(new SimpleFormatter());
    }
    private final ThreadLocal<Long>starTime = new ThreadLocal<>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    protected void beforeExecute(Thread t,Runnable r){
        super.beforeExecute(t,r);
        log.fine(String.format("Thread %s: start %s",t,r));
        starTime.set(System.nanoTime());
    }
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime-starTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.fine(String.format("Thread %s: end %s,time=%dns",t,r,taskTime));
        }finally {
            super.afterExecute(r, t);
        }
    }
    protected void terminated(){
        try{
            log.info(String.format("Terminated: avg time=%dms",totalTime.get()/numTasks.get()/1000000));
        }finally {
            super.terminated();
        }
    }
}
