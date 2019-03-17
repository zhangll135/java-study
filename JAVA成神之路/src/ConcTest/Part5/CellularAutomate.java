package ConcTest.Part5;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;

public class CellularAutomate {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomate(Board board){
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count,()->{
            if(mainBoard.promotion()<=3){//继续进化
                CellularAutomate a = new CellularAutomate(mainBoard);
                a.start();
            }
        });
        this.workers = new Worker[count];
        for(int i=0;i<count;i++)
            workers[i]=new Worker(mainBoard.getSubBoard(count,i));
    }
    private class Worker implements Runnable{
        private final Board board;
        public Worker(Board board){
            this.board=board;
        }
        public void run(){
            while (!board.hasConverged()){
                board.commitNewValues();
                try{
                    barrier.await();
                }catch (Exception e){}
            }
        }
    }
    public void start(){
        for(int i=0;i<workers.length;i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }
}
class Board{
    private int[] cells;
    private int star,end;
    private  final AtomicInteger cnt;
    public Board(){
        cells = new int[100];
        for(int i=0;i<100;i++)
            cells[i] = i;
        star=0;
        end=100;
        cnt = new AtomicInteger(1);
    }
    public Board(int cells[],int star,int end,int cnt){
        this.cells = cells;
        this.star = star;
        this.end = end;
        this.cnt = new AtomicInteger(cnt);
    }
    public void commitNewValues(){
        for(int i=star;i<end;i++)
            cells[i]+=100;
        try{Thread.sleep(40*(end-star));
        }catch (Exception e){}
        System.out.println("star:end=["+star+","+(end-1)+"]"+"=("+cells[star]+","+cells[end-1]+")");
    }
    public int promotion(){
        System.out.println("the "+cnt.get()+":Generation Cells");
        System.out.println("star:end=["+star+","+(end-1)+"]"+"=("+cells[star]+","+cells[end-1]+")");
        return cnt.incrementAndGet();
    }
    public void waitForConvergence(){
        System.out.println("Alreay Done");
    }
    public Board getSubBoard(int n,int i){
        int deta = end-star;
        return new Board(cells,star+i*deta/n,star+(i+1)*deta/n,cnt.get());
    }
    public boolean hasConverged(){
        for(int i=star;i<end;i++)
            if(cells[i]<100*cnt.get())
                return false;
        return true;
    }
}