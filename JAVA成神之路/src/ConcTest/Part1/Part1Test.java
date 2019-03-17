package ConcTest.Part1;

import ConcTest.MyAnnotation.GuardeBy;
import ConcTest.MyAnnotation.NotThreadSafe;

public class Part1Test {
    public static void main(String[] args){
        UnSafeSequence sequence = new UnSafeSequence();
        for(int i=0;i<1000;i++)
            new Thread(()->sequence.getNext()).start();

        while (Thread.activeCount()>2)
            Thread.yield();
        System.out.println("After 1000 i++: "+sequence.getNext());
    }
}

@NotThreadSafe
class UnSafeSequence{
    @GuardeBy("this")
    private int i;
    public int getNext(){
        return i++;
    }
}
