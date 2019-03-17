package ConcTest.Part10;

import ConcTest.MyAnnotation.GuardeBy;

import java.util.HashSet;

@GuardeBy("this")
public class Taxi {
    private final Dispatcher dispatcher;
    public Taxi(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }
    public synchronized void setLocation(){
        dispatcher.notifyAvailable(this);
        System.out.println("setLocation");
    }
    public synchronized void getLocation(){
        System.out.println("getLocation");
    }
}
class Dispatcher{
    @GuardeBy("this")
    private final HashSet<Taxi>taxis;
    public Dispatcher(){
        taxis=new HashSet<>();
    }
    public synchronized void notifyAvailable(Taxi taxi){
        taxis.add(taxi);
        taxi.getLocation();
        System.out.println("notifyAvailable:"+taxi);
    }
}
