package ConcTest.Part9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LongTimeFram extends JFrame {
    private JButton button;
    private JButton cancel;
    private JPanel panel;
    private JTextArea textArea;
    private final ExecutorService bgexe = Executors.newCachedThreadPool();
    Future<?> task = null;
    public LongTimeFram(){
        textArea = new JTextArea("idle\n");
        add(textArea,BorderLayout.CENTER);

        panel = new JPanel();
        add(panel,BorderLayout.SOUTH);

        button = new JButton("RunLongTask");
        cancel = new JButton("Cancel");
        cancel.setEnabled(false);
        panel.add(button);
        panel.add(cancel);

        button.addActionListener((ActionEvent event)->{
            button.setEnabled(false);
            cancel.setEnabled(true);
            textArea.setText("busy\n");           //任务一：事件任务
            task = bgexe.submit(()->{                   //任务二：后台长时任务
                try{
                    for(int i=0;i<10&&!Thread.currentThread().isInterrupted();i++) {
                        Thread.sleep(1000);//模拟长时间工作
                        textArea.append("long Time job ..."+(i+1)+"/10:job\n");
                    }
                }catch (InterruptedException e){}
                finally {
                    EventQueue.invokeLater(()->{      //任务三：事件任务
                        button.setEnabled(true);
                        cancel.setEnabled(false);
                        textArea.append("idle\n");});
                }
            });
        });

        cancel.addActionListener((ActionEvent event)->{
            if(task!=null)
                task.cancel(true);
        });


        setLocation(500,100);
        setSize(300,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
