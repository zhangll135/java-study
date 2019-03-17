package ConcTest.Part6;

import ConcTest.MyAnnotation.GuardeBy;
import ConcTest.MyAnnotation.ThreadSafe;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public interface CancellableTask<T> extends Callable<T> {
    void cancel();
    RunnableFuture<T> newTask();
}
@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor{
    public CancellingExecutor(){
        super(2,10,10, TimeUnit.MINUTES,new ArrayBlockingQueue<>(1000));
    }
    protected<T> RunnableFuture<T>  newTaskFor(Callable<T> callable){
        if(callable instanceof CancellableTask)
            return ((CancellableTask<T>)callable).newTask();
        else
            return super.newTaskFor(callable);
    }
}
abstract class SocketUsingTask<T> implements CancellableTask<T>{
    @GuardeBy("this")private Socket socket;
    protected synchronized void setSocket(Socket s){
        this.socket=s;
    }
    public synchronized void cancel(){
        try{
            if(socket==null)
                socket.close();
        }catch (IOException ignored){ }
    }
    public RunnableFuture<T> newTask(){
        return new FutureTask<T>(this){
            public boolean cancel(boolean mayInterrupIfRunning){
                try{
                    SocketUsingTask.this.cancel();
                }finally {
                    return super.cancel(mayInterrupIfRunning);
                }
            }
        };
    }
}
