package ConcTest.Part6;
import java.util.concurrent.*;

public class CompletionService<V> {
    BlockingQueue<FutureTask> completeQueue=new ArrayBlockingQueue<>(0);
    private final ExecutorService executor;
    public CompletionService(ExecutorService executor){
        this.executor = executor;
    }
    public void submit(Callable<V> image){
        executor.submit(new QueueingFuture<>(image));
    }
    public Future<V> take()throws InterruptedException{
        return completeQueue.take();
    }
    private class QueueingFuture<V> extends FutureTask<V>{
        QueueingFuture(Callable<V> c){
            super(c);
        }
        protected void done(){
            completeQueue.add(this);
        }
    }
}
