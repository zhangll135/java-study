package ConcTest.Part6;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadWebServer {
    public void run()throws IOException{
        ServerSocket socket = new ServerSocket(8089,50,Inet4Address.getByName("localhost"));
        while (true){
            Socket conn = socket.accept();
            handlesRequest(conn);
        }
    }
    private void handlesRequest(Socket conn){
        try {
            System.out.println("Response " + conn.getLocalAddress() + " : Hello World!");
            conn.getOutputStream().write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type:text/html\r\n\r\n" +
                    "<center>Hello World!\r\n").getBytes());
            conn.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
