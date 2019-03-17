package ConcTest.Part8;

import java.util.concurrent.*;

public class ThreadDeadlock {
    private ExecutorService exec;
    public void showThread(int n)throws Exception{
        exec = Executors.newFixedThreadPool(n);
        Future<String> page=exec.submit(new RendPageTask());
        System.out.println(page.get());
        exec.shutdown();
    }
    public class RendPageTask implements Callable<String>{
        public String call() throws Exception{
            Future<String> head,footer;
            head = exec.submit(new LoadFileTask("head.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            return head.get()+"\n"+page+"\n"+footer.get();
        }
        private String renderBody()throws InterruptedException{
            Thread.sleep(1000);
            return "Body";
        }
        class LoadFileTask implements Callable<String>{
            private String name;
            public LoadFileTask(String name){
                this.name=name;
            }
            public String call()throws InterruptedException{
                Thread.sleep(1000);
                return name;
            }
        }
    }

}
