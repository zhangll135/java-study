package G1Test;

public interface ISmartServerFactory{
    ISmartService creat();
}

class SmartServerFactoryImp implements ISmartServerFactory{
    public ISmartService creat(){
        ISmartService imp = new IsmartServiceImpl();
        ASceduler sceduler = new Sceduler(new SmartQueueImp());
        sceduler.start();
        return new Proxy(imp,sceduler);
    }
}
