package sorting;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.*;


/* This is the main class of this project
   This project was made by students of Kessben University College */


public abstract class MainPage extends javax.swing.JFrame {
   public static void main(String [] params) throws InterruptedException {
        final JFrame f = new JFrame("Comparative Analyses Of Sorting Algorithms");
        f.setSize(1800,2860);
       ClassAsymptotic frame1 = new  ClassAsymptotic();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/emgee.png");
        f.setIconImage(icon);
       
        ImageIcon i = new ImageIcon("src/resources/math3.jpg");
        final JLabel l = new JLabel();
        l.setIcon(i);
        final JPanel p = new JPanel();
        JButton asymptotic = new JButton(new ImageIcon("src/resources/asymptotic.jpg"));
            asymptotic.setBounds(425,225,200,70);
            asymptotic.setToolTipText("Read about sorting algorithms and asymptotic analysis");
            f.add(asymptotic);
        final JButton empirical = new JButton (new ImageIcon("src/resources/empirical.jpg"));
            empirical.setBounds(775,225,200,70);
             empirical.setToolTipText("Test and compare the speed of nine sorting algorithms");
            f.add(empirical);
            
        final JButton visualize = new JButton (new ImageIcon("src/resources/visualize.jpg"));
            visualize.setBounds(600,350,200,70);
             visualize.setToolTipText("Visualize the sorting algorithms");
            f.add(visualize);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
                 p.add(l);
        f.add(p);
        f.setVisible(true);
                    empirical.addActionListener(new java.awt.event.ActionListener(){
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt){
                          SortLabFrame frame = new SortLabFrame();
                          frame.setLocationRelativeTo(null);
                          frame.setVisible(true);
                          
                             
                           
                        }
                    
                            });
                            
                     visualize.addActionListener(new java.awt.event.ActionListener(){
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt){           
                        Program.run();
                        }
                    
                            });
                             
                             
                    asymptotic.addActionListener(new java.awt.event.ActionListener(){
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt){
                            new ClassAsymptotic().setVisible(true);
                        }
                       
                            });
                             }
}

                         
                          
                             
                        
                            
                            
                         
                            
                            
                            
                                    

        
       
     
        
                        
    
   
                    
