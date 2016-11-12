/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package Events;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author devi motors
 */
public class StackOverFlowLabelQuestion extends JFrame {

    private final JPanel mainPanel;
    private final JLabel label;
    private final Timer timer;
    int i=0;
    public StackOverFlowLabelQuestion() throws HeadlessException {
        setSize(500, 500);

        mainPanel = new JPanel();
        setContentPane(mainPanel);

        JButton start = new JButton("Start");
        
        timer = new Timer(10,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(String.valueOf(i));
                if(i==10){
                    timer.stop();
                }
                i++;
                
            }
        });
        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });
        mainPanel.add(start);

        label = new JLabel();
        mainPanel.add(label);

        setVisible(rootPaneCheckingEnabled);

    }

    public static void main(String args[]) {
        new StackOverFlowLabelQuestion();
    }
}
