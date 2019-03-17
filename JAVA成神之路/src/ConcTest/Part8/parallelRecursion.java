package ConcTest.Part8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class parallelRecursion {
    public void execute(final Executor exec, List<Node> node, final Collection<Integer> result){
        if(node==null)
            return;
        for(final Node n:node ) {
            exec.execute(() -> result.add(n.compute()));
            execute(exec, n.getChild(), result);
        }
    }
}
class Node{
    private int i;
    public Node(int i){
        this.i=i;
    }
static AtomicInteger cnt =new AtomicInteger();
    public int compute(){
        try{
            Thread.sleep(1000);
        }catch (InterruptedException ignore){
        }
        return i;
    }
    public List<Node> getChild(){
        if (cnt.get()>100)
            return null;
        List<Node> res = new ArrayList<>();
        res.add(new Node(cnt.incrementAndGet()));
        res.add(new Node(cnt.incrementAndGet()));
        return res;
    }
}
