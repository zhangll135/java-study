package ConcTest.Part6;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LogWrite {
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private volatile AtomicBoolean shutDownRequested;
    private volatile AtomicInteger cnt;
    public LogWrite(BlockingQueue<String> queue,Writer writer){
        this.queue=queue;
        this.logger=new LoggerThread(writer);
        this.shutDownRequested = new AtomicBoolean(false);
        cnt = new AtomicInteger(0);
    }
    public void start(){
        logger.start();
    }
    public void stop(){
        shutDownRequested.getAndSet(true);
    }
    public void log(String msg)throws InterruptedException{
        if(shutDownRequested.get()) {
            queue.put(msg);
            cnt.incrementAndGet();
        }
        else
            throw new IllegalStateException("logger is shut down");
    }
    class LoggerThread extends Thread{
        private final PrintWriter write;
        public LoggerThread(Writer write){
            this.write=new PrintWriter(write);
        }
        public void run(){
            try{
                while (true){
                    try {
                        if(cnt.get()==0&&shutDownRequested.get())
                            break;
                        String msg = queue.take();
                        cnt.decrementAndGet();
                        write.println(msg);
                    }catch (InterruptedException e){
                    }
                }
            }finally {
                write.close();
            }
        }
    }
}

