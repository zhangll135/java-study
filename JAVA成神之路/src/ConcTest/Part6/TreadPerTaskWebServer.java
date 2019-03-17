package ConcTest.Part6;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class TreadPerTaskWebServer {
    public void run()throws IOException {
        ServerSocket socket = new ServerSocket(8089,50, Inet4Address.getByName("localhost"));
        while (true){
            final Socket conn = socket.accept();
            new Thread(()->handlesRequest(conn)).start();
        }
    }
    private void handlesRequest(Socket conn){
        try {
            System.out.println("Response " + conn.getLocalAddress() + " : Hello World!");
            conn.getOutputStream().write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type:text/html\r\n\r\n" +
                    "<center>Hello World!\r\n").getBytes());
            Thread.sleep(1);
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}