package ConcTest.Part9;

import java.awt.*;
import java.util.concurrent.*;

public abstract class BackGroundTask<V> implements Runnable, Future<V> {
    private final FutureTask<V> computation = new Computation();
    private class Computation extends FutureTask<V>{
        public Computation(){
            super(()->BackGroundTask.this.compute());
        }
        protected final void done(){
            EventQueue.invokeLater(()->{
                V value=null;
                Throwable thrown=null;
                boolean cancel=false;
                try{
                    value=get();
                }catch (ExecutionException e){
                    thrown=e.getCause();
                }catch (CancellationException e){
                    cancel=true;
                }catch (InterruptedException e){
                }finally {
                    onCompletion(value,thrown,cancel);
                }
            });
        }
    }
    protected void setProgress(final int current,final int max){
        EventQueue.invokeLater(()->onProgress(current,max));
    }
    //后台线程取消
    public abstract V compute()throws Exception;
    //事件线程中取消
    protected void onCompletion(V result,Throwable exception,boolean canceled){
        System.out.println(result+":"+exception+":"+canceled);
    }
    protected void onProgress(int current,int max){
        System.out.println(current+"/"+max);
    }
    // Future其他方法
    @Override
    public void run() {
        computation.run();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return computation.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return computation.isCancelled();
    }

    @Override
    public boolean isDone() {
        return computation.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return computation.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return computation.get(timeout,unit);
    }
}
