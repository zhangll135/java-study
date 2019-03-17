package ConcTest.Part5;

import java.util.concurrent.CountDownLatch;

public class TestHarness{
    public long timeTasks(int nThreads,final Runnable task)throws InterruptedException{
        final CountDownLatch starGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for(int i=0;i<nThreads;i++){
            new Thread(()->{
                try{
                    starGate.await();
                    task.run();
                }catch (Exception ignore){
                }finally {
                    endGate.countDown();
                }}).start();
        }
        long start = System.nanoTime();
        starGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end-start;
    }
}
class Task implements Runnable{
    public void run(){
        System.out.println("task start");
        new leetcode.KthSmallerstPath().main(null);
    }
}
