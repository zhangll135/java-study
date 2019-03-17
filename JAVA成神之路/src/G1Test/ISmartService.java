package G1Test;

public interface ISmartService {
    void getNoResult(Object o); //写操作、无需返回
    IResult getResult(Object o); //读操作，返回结果
}

//服务实现
class IsmartServiceImpl implements ISmartService {
    public void getNoResult(Object o) {// 模拟短耗时操作
        try {
            Thread.sleep(10);
        } catch (Exception e) {
        }
        System.out.println("front job is done:  " + o);
    }

    public IResult getResult(Object o) {// 模拟长耗时操作
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        RealResult r = new RealResult(o); //立即返回实时量
        System.out.println("back long is add：" + r.getResult());
        return r;
    }
}

//代理：请求鉴权
class Proxy implements ISmartService {
    ISmartService imp;
    ASceduler sceduler;
    public Proxy(ISmartService imp, ASceduler sceduler){
        this.imp = imp;
        this.sceduler = sceduler;
    }
    public void getNoResult(Object o){// 模拟短耗时操作，
        ARequest rq = new NoResultRequest(imp,o);    // 封装请求+服务
        sceduler.invoke(rq);                        // 调度运行请求
    }
    public IResult getResult(Object o){// 模拟长耗时操作
        ARequest rq = new ResultRequest(imp,o);
        sceduler.invoke(rq);
        return rq.getResult();                       //返回取货凭证
    }
}

