package ConcTest.Part8;

import ConcTest.MyAnnotation.ThreadSafe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//谜题框架
public interface Puzzle<P,M> {
    P initialPosition();
    boolean isGoal(P position);
    HashSet<M> leagalMoves(P position);
    P move(P position,M move);
}

class PuzzleNode<P,M> {
    final P pos;
    final M move;
    final PuzzleNode<P,M>prev;
    PuzzleNode(P pos,M move,PuzzleNode<P,M> prev){
        this.pos=pos;
        this.move=move;
        this.prev=prev;
    }
    List<M> asMoveList(){
        List<M>solution = new LinkedList<>();
        for(PuzzleNode<P,M>n=this;n.move!=null;n=n.prev){
            solution.add(0,n.move);
        }
        return solution;
    }
}

class SequenceialPuzzleSolver<P,M>{
    private final Puzzle puzzle;
    private final HashSet<P> seen = new HashSet<>();
    public SequenceialPuzzleSolver(Puzzle<P,M>puzzle){
        this.puzzle = puzzle;
    }
    public List<M> solve(){
        P pos = (P)puzzle.initialPosition();
        return search(new PuzzleNode<P,M>(pos,null,null));
    }
    public List<M> search(PuzzleNode<P,M> node){
        if(!seen.contains(node)){
            seen.add(node.pos);
            if (puzzle.isGoal(node.pos)){
                return node.asMoveList();
            }else{
                for(M move:(HashSet<M>)puzzle.leagalMoves(node.pos)){
                    P pos=(P)puzzle.move(node.pos,move);
                    List<M> result = search(new PuzzleNode<>(pos,move,node));
                    if (result!=null)
                        return result;
                }
            }
        }
        return null;
    }
}
class ConcurrentPuzzleSolver<P,M>{
    private final Puzzle puzzle;
    private final ExecutorService executor = Executors.newFixedThreadPool(100);
    private final ConcurrentMap<P,Boolean> seen = new ConcurrentHashMap<>();
    private final ValueLautch<PuzzleNode<P,M>> result = new ValueLautch<>();
    private final AtomicInteger taskCount = new AtomicInteger(0);//增加结束条件
    public ConcurrentPuzzleSolver(Puzzle<P,M>puzzle){
        this.puzzle = puzzle;
    }
    public List<M> solve(){
        try{
            P pos = (P)puzzle.initialPosition();
            executor.execute(new newTask(pos,null,null));
            //阻塞，直到获取结果
            PuzzleNode solution = result.getValue();
            return (solution==null?null:solution.asMoveList());
        }catch (InterruptedException e){
            return null;
        }finally {
            executor.shutdown();
        }
    }
    class newTask extends PuzzleNode implements Runnable{
        public newTask(P p,M m,PuzzleNode pre){
            super(p,m,pre);
            taskCount.incrementAndGet();
        }
        public void run(){
            try{
                run0();
            }finally {
                if (taskCount.get()==0){
                    result.setValue(this);
                }
            }
        }
        public void run0(){
            if(result.isSet()||seen.putIfAbsent((P)pos,false)!=null){
                return;
            }else if(puzzle.isGoal(pos)){
                result.setValue(this);
            }else {
                for (M m : (List<M>) puzzle.leagalMoves(pos)) {
                    executor.execute(new newTask((P) puzzle.move(pos,m), m, this));
                }
            }
        }
    }
}

@ThreadSafe
class ValueLautch<T>{
    private T value = null;
    private final CountDownLatch cnt = new CountDownLatch(1);
    public boolean isSet(){return cnt.getCount()==0;}
    public synchronized void setValue(T newValue){
        if(!isSet()) {
            value = newValue;
            cnt.countDown();
        }
    }
    public T getValue()throws InterruptedException{
        cnt.await();
        synchronized (this){
            return value;
        }
    }
}
