package ConcTest.Part3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Part3Test {
    private static boolean ready;
    private static int number;
    private static class ReaderThread extends Thread{
        public void run(){
            while (!ready)
                yield();
            System.out.println(number);
        }
    }
    public static void main(String[]args)throws Exception{
        new ReaderThread().start();
        number = 42;
        ready = true;

        new ThisEscap(new EventSource());

        Connection con = ThreadLocal1.getConnection();
        ResultSet set = con.getMetaData().getTables(null,null,null,new String[]{"TABLE"});
        if(!set.next()){
            con.createStatement().executeUpdate("Create table Publishers(Publisher_id CHAR(6),Name CHAR(30),URL CHAR(80))");
            set = con.getMetaData().getTables(null,null,null,new String[]{"TABLE"});
            set.next();
        }
        System.out.println(set.getString(3));
        //同一共享变量，不同线程副本
        System.out.println(ThreadLocal1.getConnection().hashCode());
        System.out.println(ThreadLocal1.getConnection().hashCode());
        new Thread(()->System.out.println(ThreadLocal1.getConnection().hashCode())).start();
        new Thread(()->System.out.println(ThreadLocal1.getConnection().hashCode())).start();
    }
}
class ThisEscap{
    private int data=1;
    public void show(){System.out.println(data);}
    public ThisEscap(EventSource source){
        source.registEven(new Even());
    }
    class Even{
        public ThisEscap doSomething(int t){
            data=t;
            return ThisEscap.this;
        }
    }
}
class EventSource{
    public void registEven(ThisEscap.Even e){
        e.doSomething(100).show();//ThisEscap逸出
    }
}
class ThreadLocal1{
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){
        public Connection initialValue(){
            try {
                return DriverManager.getConnection("jdbc:derby:COREJAVA;create=true");
            }catch (Exception e){
            }
            return null;
        }
    };
    public static Connection getConnection(){
        return connectionHolder.get();
    }
}