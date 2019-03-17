package ConcTest.Part4;

import ConcTest.MyAnnotation.GuardeBy;
import ConcTest.MyAnnotation.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Chapter4 {
    public static void main(String[] args){
        CountingFactorizer c = new CountingFactorizer();
        c.service();
        System.out.println(c.getCount());

        Point p = new Point(0,1);
        System.out.println(p.x+","+p.y);
        p.map.put("lin",123);
        System.out.println(p.map.get("zhangl"));
        System.out.println(p.map.get("lin"));
        p.unmap.get("zz");
        System.out.println(p.unmap.get("zz"));
    }
}
@ThreadSafe
@GuardeBy("final safe object count")
class CountingFactorizer {
    private final AtomicLong count = new AtomicLong(0);
    public long getCount(){
        return count.get();
    }
    public void service(){
        count.incrementAndGet();
    }
}
@ThreadSafe
@GuardeBy("Imutable")
class Point{
    public final int x;
    public final int y;
    public final Map<String,Integer> map,unmap;
    public Point(int x,int y){
        this.x=x;
        this.y=y;
        map = new ConcurrentHashMap<>();
        map.put("zhangl",135);
        unmap = Collections.unmodifiableMap(map);
    }
}
@ThreadSafe("Private Constructor Capture Idiom")
class SafePoint{
    @GuardeBy("this")
    private int x,y;
    private SafePoint(int[] p){
        this(p[0],p[1]);
    }
    public SafePoint(SafePoint p){
        this(p.get());
    }
    public SafePoint(int x,int y){
        this.x = x;
        this.y = y;
    }
    public synchronized int[]get(){
        return new int[]{x,y};
    }
    public synchronized void set(int x,int y){
        this.x=x;
        this.y=y;
    }
}
