package ConcTest.Part5;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class BoundeHashSet<T>{
    private final HashSet<T> set;
    private final Semaphore sem;
    public BoundeHashSet(int bound){
        set = new HashSet<T>();
        sem = new Semaphore(bound);
    }
    public boolean add(T o)throws InterruptedException{
        sem.acquire();
        boolean wasAdded = false;
        try{
            wasAdded=set.add(o);
        }catch (Exception e){}
        finally {
            if(!wasAdded)
                sem.release();
            else
                System.out.println("add "+o.toString());
            return wasAdded;
        }
    }
    public boolean remove(Object o){
        boolean wasRemoved = set.remove(o);
        if(wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
