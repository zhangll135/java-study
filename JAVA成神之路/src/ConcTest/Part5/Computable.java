package ConcTest.Part5;

import ConcTest.MyAnnotation.GuardeBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public interface Computable<A,V> {
    V compute(A arg) throws InterruptedException;
}
class ExpresiveFunction implements Computable<String, BigInteger>{
    public BigInteger compute(String args){
        try{
            Thread.sleep(1000);
        }catch (Exception e){}
        return new BigInteger(args);
    }
}
class Memorizer1<A,V>implements Computable<A,V>{
    @GuardeBy("this")
    final private Map<A,V>cache=new HashMap<>();
    final private Computable<A,V> c;
    public Memorizer1(Computable<A,V> c){
        this.c = c;
    }
    public synchronized V compute(A arg)throws InterruptedException{
        V result = cache.get(arg);
        if(result==null){
            result = c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }
}
class Memorizer2<A,V>implements Computable<A,V>{
    @GuardeBy("concurrentmap cache")
    final private Map<A,V>cache=new ConcurrentHashMap<>();
    final private Computable<A,V> c;
    public Memorizer2(Computable<A,V> c){
        this.c = c;
    }
    public V compute(A arg)throws InterruptedException{
        V result = cache.get(arg);
        if(result==null){
            result = c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }
}
class Memorizer3<A,V>implements Computable<A,V>{
    @GuardeBy("concurrentmap cache")
    final private Map<A, Future<V>>cache=new ConcurrentHashMap<>();
    final private Computable<A,V> c;
    public Memorizer3(Computable<A,V> c){
        this.c = c;
    }
    public V compute(final A arg)throws InterruptedException{
        Future<V> result = cache.get(arg);
        if(result==null){
            result = new FutureTask<>( ()-> {
                try {
                    return c.compute(arg);
                } catch (Exception e) {
                }
                return null;
            });
            cache.put(arg,result);
            ((FutureTask<V>) result).run();
        }
        try {
            return result.get();
        }catch (ExecutionException e){
            e.printStackTrace();
            throw new InterruptedException();
        }
    }
}
class Memorizer<A,V>implements Computable<A,V>{
    @GuardeBy("concurrentmap cache")
    final private Map<A, Future<V>>cache=new ConcurrentHashMap<>();
    final private Computable<A,V> c;
    public Memorizer(Computable<A,V> c){
        this.c = c;
    }
    public V compute(final A arg)throws InterruptedException {
        while (true) {
            Future<V> result = cache.get(arg);
            if (result == null) {
                FutureTask<V> f = new FutureTask<>(()->{
                    try{
                        return c.compute(arg);
                    }catch (InterruptedException e){
                        cache.remove(this);//清除缓存
                    }
                    return null;
                });
                result = cache.putIfAbsent(arg, f);
                if(result==null){
                    result = f;
                    f.run();
                }
            }
            try {
                return result.get();
            }catch (CancellationException e1){
                cache.remove(arg);
                e1.printStackTrace();
            }
            catch (ExecutionException e) {
                cache.remove(arg);
                e.printStackTrace();
            }
        }
    }
}
