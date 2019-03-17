package G1Test;
/*
java -Xms60M -Xmx60M -Xmn20M  -XX:+PrintGCDetails -Xloggc:memgc.txt G1Test.MainTest
-XX:+UseSerialGC
-XX:+UseParallelGC
-XX:+UseParallelOldGC
-XX:+UseConcMarkSweepGC
 */
public class MainTest {
    public static void main(String[] args){
        ISmartService service = new SmartServerFactoryImp().creat();
        new NoResultThread(service).start();
        new ResultThread(service).start();
    }
}

class NoResultThread extends Thread{ //每50ms一批短时任务
    private ISmartService service;
    public NoResultThread(ISmartService service){
        this.service = service;
    }
    public void run(){
        while(true) {
            for(int i=0;i<5;i++) {
                service.getNoResult("NoResultThread：" + (i + 1));
            }
        }
    }
}

class ResultThread extends Thread{ //每50ms一批长时任务
    private ISmartService service;
    public ResultThread(ISmartService service){
        this.service = service;
    }
    public void run(){
        while(true) {
            for(int i=0;i<5;i++) {
                IResult r = service.getResult(i+1 );//返回凭证：r.getResult才能取到真正结果。
            }
        }
    }
}