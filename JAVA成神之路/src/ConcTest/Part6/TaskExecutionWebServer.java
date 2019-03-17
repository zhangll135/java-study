package ConcTest.Part6;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {
    private static final Executor exec = Executors.newFixedThreadPool(100);
    public void run() throws IOException {
        ServerSocket socket = new ServerSocket(8089, 50, Inet4Address.getByName("localhost"));
        while (true) {
            final Socket conn = socket.accept();
            exec.execute(() -> handlesRequest(conn));
        }
    }

    private void handlesRequest(Socket conn) {
        try {
            System.out.println("Response " + conn.getLocalAddress() + " : Hello World!");
            conn.getOutputStream().write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type:text/html\r\n\r\n" +
                    "<center>Hello World!\r\n").getBytes());
            Thread.sleep(1);
            conn.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}