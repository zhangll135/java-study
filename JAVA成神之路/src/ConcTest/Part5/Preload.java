package ConcTest.Part5;

import java.util.concurrent.FutureTask;

public class Preload{
    private final FutureTask<ProductInfo> future=new FutureTask<>(()->loadProductInfo());
    private final Thread thread = new Thread(future);
    private ProductInfo loadProductInfo()throws DataLoadException{
        System.out.println("will done in 1 seconds");
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            throw new DataLoadException();
        }
        return new ProductInfo("bag",1111);
    }
    public void start(){
        thread.start();
    }
    public ProductInfo get() throws DataLoadException,InterruptedException{
        try{
            return future.get();
        }catch (Exception e){
            Throwable cause = e.getCause();
            if(cause instanceof DataLoadException)
                throw (DataLoadException)e;
            else
                throw launderThrowable(cause);
        }
    }
    public static RuntimeException launderThrowable(Throwable t){
        if(t instanceof RuntimeException)
            throw (RuntimeException) t;
        else if(t instanceof Error)
            throw (Error)t;
        else
            throw new IllegalStateException("Not uncaughted",t);
    }
}
class ProductInfo{
    public final String name;
    public final int id;
    public ProductInfo(String name,int id){
        this.name=name;
        this.id=id;
    }
}
class DataLoadException extends Exception{
}
