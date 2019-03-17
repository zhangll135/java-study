package ConcTest.Part6;

import java.io.*;
import java.util.concurrent.*;

public class LogServer {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final PrintWriter writer;
    private void start(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                LogServer.this.stop();
            }catch (InterruptedException ignored){}
        }));
    }
    public LogServer()throws IOException {
        File file=new File("logger.log");
        if(!file.exists())
            file.createNewFile();
        writer=new PrintWriter(file);
    }
    public void stop()throws InterruptedException{
        try{
            exec.shutdown();
            exec.awaitTermination(10, TimeUnit.SECONDS);
        }finally {
            writer.close();
        }
    }
    public void log(String msg){
        try{
            exec.execute(()->writer.println(msg));
        }catch (RejectedExecutionException e){}
    }
}
