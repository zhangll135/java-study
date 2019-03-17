package G1Test;
//请求容器
public interface ISmartQueue{
    void put(ARequest rq);
    ARequest get();
}

class SmartQueueImp implements ISmartQueue{
    private final int max_size=1000;
    private int head;
    private int rear;
    private volatile int count;
    ARequest request[];
    public SmartQueueImp(){
        request = new ARequest[max_size];
        head=rear=count=0;
    }
    public synchronized void put(ARequest o){
        while (count==max_size){
            try{
                wait();
            }catch (Exception e){

            }
        }
        count++;
        request[(rear++)%max_size] = o;
        notifyAll();
    }
    public synchronized ARequest get(){
        while (count==0){
            try{
                wait();
            }catch (Exception e){

            }
        }
        count--;
        ARequest rq = request[head%max_size];
        request[head++%max_size] = null;
        notifyAll();
        return  rq;
    }
}
