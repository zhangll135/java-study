package ConcTest.Part9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class LongTimeFram2 extends JFrame {
    private JButton button;
    private JButton cancel;
    private JPanel panel;
    private JTextArea textArea;
    private final ExecutorService bgexe = Executors.newCachedThreadPool();
    Future<?> task = null;
    public LongTimeFram2(){
        textArea = new JTextArea("idle\n");
        add(textArea,BorderLayout.CENTER);

        panel = new JPanel();
        add(panel,BorderLayout.SOUTH);

        button = new JButton("RunLongTask");
        cancel = new JButton("Cancel");
        cancel.setEnabled(false);
        panel.add(button);
        panel.add(cancel);

        button.addActionListener((ActionEvent event)-> {
            button.setEnabled(false);
            cancel.setEnabled(true);
            textArea.setText("busy\n");           //任务一：事件任务

            class CancelListener implements ActionListener {
                BackGroundTask<?> task;
                public void actionPerformed(ActionEvent event1) {
                    if (task != null)
                        task.cancel(true);
                }
            }

            final CancelListener listen = new CancelListener();
            listen.task = new BackGroundTask<Void>() {
                @Override
                public Void compute() throws Exception {
                    for (int i = 0; i < 10 && !isCancelled(); i++) {
                        setProgress(i, 10);
                        Thread.sleep(1000);//模拟长时间工作
                    }
                    return null;
                }

                @Override
                protected void onProgress(int current, int max) {
                    textArea.append("long Time job ..." + (current + 1) + "/" + max + ":job\n");
                }

                @Override
                protected void onCompletion(Void result, Throwable exception, boolean canceled) {
                    cancel.removeActionListener(listen);
                    if (canceled) {
                        textArea.append("cancel!");
                    }else if(exception!=null){
                        textArea.append(exception.getMessage());
                    }else{
                        textArea.append("done");
                    }
                    cancel.setEnabled(false);
                    button.setEnabled(true);
                }
            };
            cancel.addActionListener(listen);
            bgexe.execute(listen.task);
        });


        setLocation(500,100);
        setSize(300,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
