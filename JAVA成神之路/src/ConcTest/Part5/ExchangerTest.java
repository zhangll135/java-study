package ConcTest.Part5;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;

public class ExchangerTest {
    final private AtomicInteger count;
    final private Exchanger exchanger= new Exchanger();
    final private String buffer[];
    public ExchangerTest(){
        count =  new AtomicInteger(-1);
        buffer = new String[]{"Hello world","zhanglin","Concurrent","what you do"};
        Read read = new Read();
        Write write = new Write();
        new Thread(()->{
            try{
                while (read.execute(buffer,count)) {
                    exchanger.exchange(write);
                }
            }catch (Exception e){}
        }).start();
        new Thread(()->{
            try{
                while (write.execute(buffer,count)){
                    exchanger.exchange(read);
                }
            }catch (Exception e){}
        }).start();
    }
}
class Read{
    public boolean execute(String[] buffer ,AtomicInteger count){//模拟读取
        if(count.get()>=0&&count.get()<buffer.length)
            System.out.println(buffer[count.get()]);
        return count.get()<buffer.length?true:false;
    }
}
class Write{
    public boolean execute(String[] buffer ,AtomicInteger count){//模拟写入1ms
        if (count.get()<buffer.length){
            try {
                Thread.sleep(10);
            }catch (Exception e){}
            count.incrementAndGet();
            return true;
        }
        return false;
    }
}