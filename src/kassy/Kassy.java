/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kassy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author student
 */
public class Kassy extends JFrame {
        public static int clientsPerHour = 3000;
        public static int secondsPerClient = 180;
        private JLabel[] kassStatus = new JLabel[5];
        private JButton[] kassToggle = new JButton[5];
        public static JTextArea queueArea = new JTextArea();
        private JPanel setupPanel = new JPanel();
        private JPanel kassPanel = new JPanel();
        private JLabel labelClientRate = new JLabel("Avg. number of clients per hour",JLabel.CENTER);
        private JLabel labelServiceTime = new JLabel("Avg. service time per client (in seconds)",JLabel.CENTER);
        private JTextField avgClientRate = new JTextField(" " + clientsPerHour);
        private JTextField avgServiceTime = new JTextField(" " + secondsPerClient);
        private JButton setupButton = new JButton("Update Settings");
        private JButton startStopButton = new JButton("Start");
        
        public static ExecutorService executor = Executors.newFixedThreadPool(5);
        public static BlockingQueue<Client> queue = new LinkedBlockingQueue<>();
        Kassa kassa = new Kassa();

    public Kassy() {
        super("Моделирование работы касс");
        
        setupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clientsPerHourLocal = 0; // =0 ne bylo
                int secondsPerClientLocal = 0;
                try{
                clientsPerHourLocal = Integer.parseInt(avgClientRate.getText().trim());
                secondsPerClientLocal = Integer.parseInt(avgServiceTime.getText().trim());
                }catch(NumberFormatException nfe){
                    avgClientRate.setText("  " + clientsPerHour);
                    avgServiceTime.setText("  " + secondsPerClient);
                }
                clientsPerHour = clientsPerHourLocal;
                secondsPerClient = secondsPerClientLocal;
            }
        });
        queueArea.setLineWrap(true);
        queueArea.setWrapStyleWord(true);
        queueArea.setEditable(false);
        
        
        
        
        
        setLayout(new BorderLayout());
        kassPanel.setLayout(new GridLayout(2,5));
        for(int i = 0; i < 5; i++) {
            kassStatus[i] = new JLabel("N" + i + ":Idle",JLabel.CENTER);
            kassPanel.add(kassStatus[i]);
        }
        for(int i = 0; i < 5; i++) {
            kassToggle[i] = new JButton("Start work");
            kassToggle[i].setActionCommand("" + (i + 1));
            kassToggle[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int command = Integer.parseInt(e.getActionCommand());
                    if(command > 0){
                        kassa.stop = false;
                        kassStatus[command-1].setText("Num " + command + " : Free" + queue);
                        kassToggle[command-1].setActionCommand("-" + command);
                        kassToggle[command-1].setText("Stop work");
                        executor.execute(kassa);
                        
                        
                        
                    }else{
                        kassa.stop = true;
                        command = - command;
                        int i = 0;
                        while(i<10){
                        i++;
                        kassStatus[command-1].setText("Num " + command + " : Idle " );//queue.remove()
                        }
                        kassToggle[command-1].setActionCommand("" + command);
                        kassToggle[command-1].setText("Start work");
                        executor.execute(kassa);
                        
                        
                        
                    }
                }
            });
            kassPanel.add(kassToggle[i]);
        }
        
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch(command){
                    case "Start":
                        
                        
                        startStopButton.setText("Stop");
                        NewClient.stopThreadClient = true;
                        new NewClient().start();
                        
                        
                        break;
                    case "Stop":
                        startStopButton.setText("Start");
                        NewClient.stopThreadClient = false;
                        
                        break;
                }
            }
        });
        
        setupPanel.setLayout(new GridLayout(2,3));
        setupPanel.add(labelClientRate);
        setupPanel.add(labelServiceTime);
        setupPanel.add(setupButton);
        setupPanel.add(avgClientRate);
        setupPanel.add(avgServiceTime);
        setupPanel.add(startStopButton);
        queueArea.setFont(new Font("Mono",Font.PLAIN,14));
        //queueArea.setText("Client[1], Client[2] ...");
        add(kassPanel,BorderLayout.NORTH);
        add(queueArea,BorderLayout.CENTER);
        add(setupPanel,BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 390);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Kassy().setVisible(true);
            }
        });
    }
    
    public static long getRandomClientDelay() {
        double avgTime = 3600/clientsPerHour;
        return (long)((Math.random() + 0.5)*avgTime);
    }
    
    public static long getRandomServiceTime() {
        return (long)((Math.random() + 0.5)*secondsPerClient);
    }
    
}
