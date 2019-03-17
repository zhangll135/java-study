package ConcTest.Part8;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class MainTest {
    public static void main(String[] args){
        TestInstaller.process(MainTest.class);
    }
    @Test(-1)
    public void ThreadDeadlockTest()throws Exception{
        System.out.println("2 Thread is normal");
        new ThreadDeadlock().showThread(2);

    }
    @Test(-2)
    public void BoundedExecutorTest()throws Exception{
        Executor exec = Executors.newFixedThreadPool(100);
        int bound = 1000;
        BoundedExecutor executor = new BoundedExecutor(exec,bound);
        for (int i=0;i<2000;i++){
            final int k = i;
            executor.submitTask(()->{
                System.out.println("Hello "+k);
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                }
            });
        }

        ((ExecutorService) exec).shutdown();
    }
    @Test(-3)
    public void TimiingThreadPool()throws Exception{
        TimingThreadPool pool = new TimingThreadPool(10,100);
        try {
            for (int i = 0; i < 100; i++) {
                final int k = i;
                pool.submit(() -> {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Hello " + k);
                });
            }
        }finally {
            pool.shutdown();
        }

    }
    @Test(4)
    public void parallelRecursionTest()throws Exception{
        Executor exec = Executors.newFixedThreadPool(100);
        List<Node> node = new Node(0).getChild();
        Collection<Integer> result = new ConcurrentLinkedQueue<>();
        new parallelRecursion().execute(exec,node,result);
        ((ExecutorService) exec).shutdown();
        ((ExecutorService) exec).awaitTermination(1000, TimeUnit.SECONDS);
        System.out.println(result);
    }
}
