package G1Test;
// 运行请求：注册任务，稍后执行
public abstract class ASceduler extends Thread{
    ISmartQueue queue;//任务容器
    public ASceduler(ISmartQueue queue){
        this.queue=queue;
    }
    public abstract void invoke(ARequest rq);
    public abstract void run();
}

class Sceduler extends ASceduler {
    final int maxthread=1000;
    final ThreadGroup group;
    public Sceduler(ISmartQueue queue){
        super(queue);
        group = new ThreadGroup("BackThreadGroup");
    }
    public void invoke(ARequest rq){//注册任务
        if(rq instanceof ResultRequest&&group.activeCount()>=maxthread) {//后台已满，拒绝服务
            System.out.println("-------------reject the job!-----------");
            return;
        }
        //System.out.println(group.activeCount());//查看后台任务量
        queue.put(rq);
    }
    public void run(){             //执行+调度（前台、后台）
        while (true){
            ARequest rq = queue.get();
            if(rq instanceof ResultRequest)                //长时间消耗（后台）
                new Thread(group,() ->rq.execute()).start();
            else                                           //立即任务（前台）
                rq.execute();
        }
    }
}
