package ConcTest.Part5;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;
import javax.servlet.Servlet;
import java.math.BigInteger;

public class MainTest {
    public static void main(String[] args) throws Exception {
        TestInstaller.process(MainTest.class);
    }

    @Test(1)
    public void HarnessTest() throws Exception {
        TestHarness harness = new TestHarness();
        System.out.println(harness.timeTasks(2, new Task()));
    }

    @Test(2)
    public void FutureTaskTest() throws Exception {
        Preload preload = new Preload();
        preload.start();//wait until 1 seconds later
        System.out.println(preload.get().name + "+" + preload.get().id);
    }

    @Test(3)
    public void SemaphoreTest() throws Exception {
        //设置1个资源，必须等到删除操作，方能继续添加
        Object o = new Object();
        BoundeHashSet<Object> set = new BoundeHashSet<>(1);
        set.add(o);
        new Thread(() -> {//wait until 3 seconds remove the preload
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            set.remove(o);
        }).start();
        set.add(o);
    }

    @Test(4)
    public void CellularAutomataTest() {
        Board board = new Board();
        CellularAutomate automate = new CellularAutomate(board);
        automate.start();
    }

    @Test(5)
    public void ExchangerTest0() {
        ExchangerTest test = new ExchangerTest();
    }

    @Test(6)
    public void Memorize1Test()throws Exception {//同步容器，单线程慢
        Memorizer1<String, BigInteger> memorizer1=new Memorizer1<>(new ExpresiveFunction());
        String args[] = new String[]{"1","2","3","4"};
        for(int i=0;i<args.length;i++){
            final int j=i;
            new Thread(()->{
                try {System.out.println(memorizer1.compute(args[j]));
                }catch (Exception e){}
            }).start();
        }
    }
    @Test(7)
    public void Memorize2Test()throws Exception {//并发容器，多线程快,但重复计算
        Memorizer2<String, BigInteger> memorizer1=new Memorizer2<>(new ExpresiveFunction());
        String args[] = new String[]{"1","1","2","2","3","3","4","4"};
        for(int i=0;i<args.length;i++){
            final int j=i;
            new Thread(()->{
                try {System.out.println(memorizer1.compute(args[j]));
                }catch (Exception e){}
            }).start();
        }
    }
    @Test(8)
    public void Memorize3Test()throws Exception {//并发容器，多线程快，不重复计算
        Memorizer3<String, BigInteger> memorizer1=new Memorizer3<>(new ExpresiveFunction());
        String args[] = new String[]{"1","1","2","2","3","3","4","4"};
        for(int i=0;i<args.length;i++){
            final int j=i;
            new Thread(()->{
                try {System.out.println(memorizer1.compute(args[j]));
                }catch (Exception e){}
            }).start();
        }
    }
    @Test(9)
    public void MemorizeTest()throws Exception {//并发容器，多线程快，不重复计算，消除污染
        Memorizer<String, BigInteger> memorizer1=new Memorizer<>(new ExpresiveFunction());
        String args[] = new String[]{"1","1","2","2","3","3","4","4"};
        for(int i=0;i<args.length;i++){
            final int j=i;
            new Thread(()->{
                try {System.out.println(memorizer1.compute(args[j]));
                }catch (Exception e){}
            }).start();
        }
    }
    @Test(10)
    public void Factorizer()throws Exception {//测试因式分解
        Servlet servlet = new Factorizer();
        for(int i=0;i<2;i++)
            new Thread(()->{
                try{servlet.service(null,null);}
                catch (Exception e){}
            }).start();
    }
}
