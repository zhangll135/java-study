package ConcTest.Part10;

import ConcTest.MyAnnotation.GuardeBy;

import java.util.HashSet;

@GuardeBy("this")
public class Taxi1 {
    private final Dispatcher1 dispatcher;
    public Taxi1(Dispatcher1 dispatcher){
        this.dispatcher = dispatcher;
    }
    public void setLocation(){
        synchronized (this) {
            System.out.println("setLocation");
        }
        dispatcher.notifyAvailable(this);//调用外部方法时，不持有锁
    }
    public synchronized void getLocation(){
        System.out.println("getLocation");
    }
}

class Dispatcher1{
    @GuardeBy("this")
    private final HashSet<Taxi1>taxis;
    public Dispatcher1(){
        taxis=new HashSet<>();
    }
    public void notifyAvailable(Taxi1 taxi){
        synchronized (this) {
            System.out.println("notifyAvailable:"+taxi);
            taxis.add(taxi);
        }
        taxi.getLocation();//调用外部方法时不持有锁
    }
}
