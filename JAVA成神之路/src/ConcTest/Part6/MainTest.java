package ConcTest.Part6;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;
import java.util.concurrent.TimeUnit;

public class MainTest {
    public static void main(String[] args) throws Exception {
        TestInstaller.process(MainTest.class);
    }

    @Test(-1)
    public void SingleThreadWebServerTest() throws Exception {
        new SingleThreadWebServer().run();
    }

    @Test(-2)
    public void ThreadPerWebServerTest() throws Exception {
        new TreadPerTaskWebServer().run();
    }

    @Test(-3)
    public void TaskExecutionWebServerTest() throws Exception {
        new TaskExecutionWebServer().run();
    }

    @Test(-4)
    public void LifeCycleWebServer() throws Exception {
    }

    @Test(-5)
    public void charTest() throws Exception {
        System.out.println((char) 21);
        System.out.println('\r' + 0);
        System.out.println('\n' + 0);
        String str = new String("file-%E5%B7%A5%E4%BD%9C%E4%BA%A4%E6%8E%A5.docx");
        byte b[] = new byte[str.length()];
        int j = 0;
        for (int i = 0; i < str.length(); ) {
            if (str.charAt(i) != '%')
                b[j++] = (byte) str.charAt(i++);
            else {
                String a = str.substring(i + 1, i + 3);
                b[j++] = (byte) Integer.parseInt(str.substring(i + 1, i + 3), 16);
                i += 3;
            }
        }
        byte a = (byte) Integer.parseInt("E5", 16);
        System.out.println(a);
        System.out.println(new String(b, 0, j, "UTF-8"));
    }

    @Test(-6)
    public void socketTest() throws Exception {
        SocketTest.main(null);
    }

    @Test(-7)
    public void HiddenIterator() {
        new HiddenIterator().addTenThings();
    }

    @Test(-8)
    public void OutOfTime() throws Exception {
        OutOfTime.main(null);
    }

    @Test(-9)
    public void aSecondsCancelTest() throws Exception {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try {
            Thread.sleep(1000);
        } finally {
            generator.cancel();
        }
        System.out.println(generator.get());
    }

    @Test(-10)
    public void NoCancelTest() throws InterruptedException {
        new PrimeGenerator().consumePrimes();
    }

    @Test(-11)
    public void InterruptTest() throws InterruptedException {
        new PrimeProduce().consumePrimes();
    }

    @Test(-12)
    public void NextTaskTest() throws Exception {
        NextTask task = new NextTask();
        Thread.currentThread().interrupt();
        System.out.println(task.getNextTask());
        Thread.currentThread().interrupt();
    }

    @Test(-13)
    public void TimedRunTest() {
        new TimeRun().timedRun(new Task(), 1000, TimeUnit.MILLISECONDS);
    }
    @Test(-14)
    public void TimedRunTest2() throws Exception{
        TimeRun2.timedRun(new Task(), 1000, TimeUnit.MILLISECONDS);
    }
    @Test(15)
    public void TimedRunTest3() throws Exception{
        TimeRun3.timedRun(new Task(), 1000, TimeUnit.MILLISECONDS);
        TimeRun3.shutDown();
    }
}
class Task implements Runnable{
    public void run(){
        try {
            while (true){
                System.out.println("OK!");
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Task Exit By Interrupted");
        }finally {
            System.out.println("Task already exit");
        }
    }
}
