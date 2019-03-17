package HttpClient;

import ConcTest.MyAnnotation.Test;
import ConcTest.MyAnnotation.TestInstaller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args)throws Exception{
        TestInstaller.process(MainTest.class);
    }
    @Test(-1)
    public void HttpClientTest()throws Exception{
        System.out.println("Hello");
        String sendRecvGet =HttpClient.sendGet("http://"+Inet4Address.getLocalHost()+"/get","param=xxxxx嘻嘻嘻");
        System.out.println(sendRecvGet);
        String sendRecvPost =HttpClient.sendPost("http://"+Inet4Address.getLocalHost()+"/post","param=就是我");
        System.out.println(sendRecvPost);
        String RecvPost =HttpClient.sendPost("http://"+Inet4Address.getLocalHost()+"/delete","param=就是我");
        System.out.println(RecvPost);
    }
    @Test(2)
    public void HttpClientCmd()throws  Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("get[/config.txt],post,delete[/config.txt],ls[ morefile],exit?");
            String line = reader.readLine();
            if("exit".equals(line))
                return;
            HttpClient.execute(line,"45.32.12.136","8089");
        }
    }
    @Test(-3)
    public void UTF8Test()throws Exception{
        System.out.println(Integer.toString(255,16));
    }
}
