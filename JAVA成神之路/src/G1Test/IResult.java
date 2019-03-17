package G1Test;
// 请求结果
public interface IResult {
    Object getResult();
}

class RealResult implements IResult {
    Object o;
    byte data[];
    public RealResult(Object o ){
        this.o = "Real Data"+o;
        data = new byte[1024*10];
    }
    public Object getResult(){
        return o;
    }
}

class FutureResult implements IResult {
    private volatile boolean completed;
    private IResult result;
    public final synchronized void setResult(IResult result){
        this.result = result;
        completed = true;
        notifyAll();
    }
    public final synchronized Object getResult(){
        while (!completed){
            try{
                wait();
            }catch (Exception e){
            }
        }
        return result.getResult();
    }
}
