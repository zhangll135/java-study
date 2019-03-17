package G1Test;
// 请求
public abstract class ARequest {
    IResult result;         //封装了取货凭证+服务方法
    ISmartService ismartService;
    public ARequest(ISmartService ismartService){
        this.ismartService = ismartService;
        this.result = new FutureResult();
    }
    public IResult getResult(){
        return result;
    }
    public abstract void execute();
}

class NoResultRequest extends ARequest {
    Object o ;
    public NoResultRequest(ISmartService iSmartService,Object o){
        super(iSmartService);
        this.o = o;
    }
    public void execute(){
        ismartService.getNoResult(o);
    }
}

class ResultRequest extends ARequest {
    Object o ;
    public ResultRequest(ISmartService iSmartService,Object o){
        super(iSmartService);
        this.o = o;
    }
    public void execute(){
        IResult t = ismartService.getResult(o);
        ((FutureResult)result).setResult(t);
        System.out.println("Back job is finished: "+result.getResult());   //回调
    }
}
