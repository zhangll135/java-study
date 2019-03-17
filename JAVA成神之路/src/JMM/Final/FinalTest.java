package JMM.Final;
/*
final 只能初始化或构造函数中初始化
final引用中内容可以改变，但线程不安全
不允许this逸出
 */
public class FinalTest {

    public static synchronized void main(String[] args)throws Exception{
        FinalTest test = new FinalTest();
        System.out.println("this 逸出：real没有构造正确");
        test.test1();
        Thread.sleep(100);

        System.out.println("\n修改final引用对象不安全、构造安全");
        test.test2();
    }
    private void test1()throws Exception{
        Thread r1 = new Thread(()->System.out.println("正确应为1:"+(A.obj!=null?A.obj.real:null)));
        Thread r2 = new Thread(()->new A());
        r2.start();
        r1.start();
    }
    private void test2()throws Exception{

        A test = new A();
        Thread r = new Thread(()->test.write());
        int data[] = new int[3];
        data[0] = test.ref[0];

        r.start();            //并发
        data[1] = test.ref[0];
        r.join();             //同步
        data[2] = test.ref[0];

        System.out.println("初始化为1："+data[0]+"\n并发修改2："+data[1]+"\n同步修改2："+data[2]);
    }

}
class A{
    static A obj;
    final int real;
    final int[] ref;
    public A(){
        obj=this;            //错误+不允许this逸出
        ref = new int[]{1};    //保证所有线程可见
        try {
            Thread.sleep(10);
        }catch (Exception e){
        }
        real = 1;
    }
    public void write(){
        ref[0] = 2;          //不能保证可见性
    }

}
