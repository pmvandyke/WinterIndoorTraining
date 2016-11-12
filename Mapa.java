//package mapa;

import java.awt.*;
import javax.swing.*;

public class Mapa extends JFrame {
  private static JPanel pan;
  private static JLabel lab;

  public Mapa() {
  }

  private static void createAndShowGUI() {
    Mapa frame = new Mapa();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    lab = new JLabel("TEXTO");
    lab.setBackground(Color.black);
    lab.setForeground(Color.white);
    lab.setOpaque(true);
    lab.setVisible(true);
 
    pan = new JPanel();
    pan.setLayout(null);
    pan.setPreferredSize(new Dimension(640,480));
    pan.add(lab);
    lab.setBounds(100,100,200,30);

    frame.getContentPane().add(pan, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }
}