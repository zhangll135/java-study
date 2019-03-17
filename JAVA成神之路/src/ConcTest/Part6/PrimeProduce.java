package ConcTest.Part6;

import ConcTest.MyAnnotation.GuardeBy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PrimeProduce implements Runnable{
    @GuardeBy("this")
    private final BlockingQueue<BigInteger> primes;
    private volatile Thread runThread= null;
    public PrimeProduce(BlockingQueue<BigInteger> primes){
        this.primes = primes;
    }
    public PrimeProduce(){
        this.primes = new ArrayBlockingQueue<>(1000);
    }
    public void run(){
        try {
            runThread = Thread.currentThread();
            BigInteger p = BigInteger.ONE;
            while (!runThread.isInterrupted()) {
                p = p.nextProbablePrime();
                primes.put(p);
            }
        }catch (InterruptedException e){
        }
    }
    public void cancel(){
        runThread.interrupt();
    }
    public synchronized List<BigInteger> get(){
        return new ArrayList<>(primes);
    }
    public void consumePrimes()throws InterruptedException{
        BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<>(100);
        PrimeProduce generator=new PrimeProduce(queue);
        new Thread(generator).start();
        try{
            int i=0;
            while (i++<5){
                System.out.println(queue.take());
                Thread.sleep(1000);
            }
        }finally {
            System.out.println(generator.get());
            generator.cancel();
        }
    }
}
