package ConcTest.Part6;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class SocketTest {
    static Thread connect;
    public static void main(String[] args)throws Exception{
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton interrupbtn = new JButton("interrupt");
        JButton blocking = new JButton("blocking");
        JButton cancel = new JButton("cancel");

        frame.add(panel);
        panel.add(interrupbtn);
        panel.add(blocking);
        panel.add(cancel);
        interrupbtn.addActionListener((ActionEvent event)->{
            interrupbtn.setEnabled(false);
            blocking.setEnabled(false);
            cancel.setEnabled(true);
            connect = new Thread(()->{
                try{
                    SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost",80));
                    Scanner in = new Scanner(channel);
                    while (!Thread.currentThread().isInterrupted()){
                        if(in.hasNextLine()){
                            System.out.println(in.next());
                        }
                    }
                }catch (Exception e){}
                finally {
                    EventQueue.invokeLater(()->{
                        interrupbtn.setEnabled(true);
                        blocking.setEnabled(true);
                    });
                }
            });
            connect.start();
        });
        blocking.addActionListener((ActionEvent event)->{
            interrupbtn.setEnabled(false);
            blocking.setEnabled(false);
            cancel.setEnabled(true);
            connect = new Thread(()->{
                try{
                    Socket channel = new Socket("localhost",80);
                    Scanner in = new Scanner(channel.getInputStream());
                    while (!Thread.currentThread().isInterrupted()){
                        if(in.hasNextLine()){
                            System.out.println(in.next());
                        }
                    }
                }catch (Exception e){}
                finally {
                    EventQueue.invokeLater(()->{
                        interrupbtn.setEnabled(true);
                        blocking.setEnabled(true);
                    });
                }
            });
            connect.start();
        });
        cancel.addActionListener((ActionEvent event)->{
            cancel.setEnabled(false);
            connect.interrupt();;
        });

        new Thread(new TestService()).start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
class TestService implements Runnable {
    public void run() {
        try{
            ServerSocket socket = new ServerSocket(80);
            while (true){
                Socket incoming = socket.accept();
                new Thread(()->{
                    try {
                        OutputStream out = incoming.getOutputStream();
                        PrintWriter writer = new PrintWriter(out,true);
                        int count=0;
                        while (count++<100){
                            if(count<=10)
                                writer.println(count);
                            Thread.sleep(100);
                        }
                    }catch (Exception e){}
                    finally {
                        try {
                            incoming.close();
                            System.out.println("Closing Server");
                        }catch (Exception e){}
                    }
                }).start();
            }
        }catch (IOException e){
            System.out.println("TestService.run: "+e);
        }
    }
}
