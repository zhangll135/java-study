package ConcTest.Part6;

import ConcTest.MyAnnotation.GuardeBy;
import ConcTest.MyAnnotation.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ThreadSafe
public class PrimeGenerator implements Runnable {
    @GuardeBy("this")
    private final BlockingQueue<BigInteger> primes;
    private volatile boolean cancelled;
    public PrimeGenerator(BlockingQueue<BigInteger> primes){
        this.primes = primes;
    }
    public PrimeGenerator(){
        this.primes = new ArrayBlockingQueue<>(1000);
    }
    public void run(){
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled) {
                p = p.nextProbablePrime();
                primes.put(p);
            }
        }catch (InterruptedException e){};
    }
    public void cancel(){
        cancelled = true;
    }
    public synchronized List<BigInteger>get(){
        return new ArrayList<>(primes);
    }
    public void consumePrimes()throws InterruptedException{
        BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<>(100);
        PrimeGenerator generator=new PrimeGenerator(queue);
        new Thread(generator).start();
        try{
            int i=0;
            while (i++<5){
                System.out.println(queue.take());
                Thread.sleep(1000);
            }
        }finally {
            generator.cancel();
            System.out.println(generator.get());
        }
    }
}
