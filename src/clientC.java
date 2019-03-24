import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class clientC{

    public JTextArea incoming;
    public JTextField outgoing;
    public PrintWriter writer;
    public BufferedReader reader;
    public Socket socket;

    public void start(){
        JFrame frame = new JFrame("Chat");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendButtonListener());
        mainPanel.add(scroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(800,350);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            socket = new Socket("localhost", 6000);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Runnable run = new MyRun();
            Thread thread = new Thread(run);
            thread.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class sendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                writer.println(outgoing.getText());
            }catch(Exception ex){
                ex.printStackTrace();
            }

            outgoing.setText("");

        }

    }

    public class MyRun implements Runnable{

        @Override
        public void run() {
            System.out.println("2 поток запущен");
            try {
                while(true) {
                    String str;
                    if((str = reader.readLine()) != null){
                        System.out.println(str);
                        incoming.append(str + "\n");
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }


}
