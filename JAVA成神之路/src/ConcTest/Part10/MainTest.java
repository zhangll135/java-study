package ConcTest.Part10;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;

public class MainTest {
    public static void main(String[] args){
        TestInstaller.process(MainTest.class);
    }
    @Test(-1)
    public void LockOrderDeadLockTest(){
        Account from = new Account();
        Account to = new Account();
        int dollar = 10;
        for(int i=0;i<1000;i++){
            new Thread(()->new LockOrderDeadLock().transferMony(from,to,dollar)).start();
            new Thread(()->new LockOrderDeadLock().transferMony(to,from,dollar)).start();
        }
        while (Thread.activeCount()>3){
            Thread.yield();
        }
    }
    @Test(-2)
    public void LockOrderDeadLockTest2(){
        Account from = new Account();
        Account to = new Account();
        int dollar = 10;
        for(int i=0;i<1000;i++){
            new Thread(()->new LockOrderDeadLockHash().transferMony(from,to,dollar)).start();
            new Thread(()->new LockOrderDeadLockHash().transferMony(to,from,dollar)).start();
        }
        while (Thread.activeCount()>3){
            Thread.yield();
        }
    }
    @Test(3)
    public void TaxiTest(){
        Dispatcher dispatcher = new Dispatcher();
        Taxi taxi=new Taxi(dispatcher);
        for(int i=0;i<1000;i++){
            new Thread(()->taxi.setLocation()).start();
            new Thread(()->dispatcher.notifyAvailable(taxi)).start();
        }
    }
    @Test(4)
    public void TaxiTest1(){
        Dispatcher1 dispatcher = new Dispatcher1();
        Taxi1 taxi=new Taxi1(dispatcher);
        for(int i=0;i<1000;i++){
            new Thread(()->taxi.setLocation()).start();
            new Thread(()->dispatcher.notifyAvailable(taxi)).start();
        }
    }
}
