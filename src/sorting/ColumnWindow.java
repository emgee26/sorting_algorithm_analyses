package sorting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


import javax.swing.JLabel;


/**
 * Class for window which draws the column charts.
 * KUC Group 3
 */
public class ColumnWindow extends JFrame {
    private ColumnWindow thisWindow = this;
    private JComboBox sortingAlgorithms = new JComboBox();
    private JLabel saLabel = new JLabel("Sorting algorithm");
    private JComboBox counts = new JComboBox();
    private JLabel cLabel = new JLabel("Number of values");
    private int [] countArray = {2, 3, 4, 5, 8, 10, 15, 20, 30, 40, 50, 60, 80, 100, 150, 250, 375, 750, 1000};
    private List<Column> columns;
    private ColumnPanel [] panels = new ColumnPanel[2];
    private int sortingSleepTime;
    private Random rnd = new Random();
    Color backgroundColor = new Color(255,255,255);


    /**
     * Subclass for running the sorting thread.
     */
    private class Execution implements Runnable {
        @Override
        public void run() {
           
            panels[1].GetColumns().Sort(true);
        
       
           
           
        }
    }
    
      
    private Thread sortingThread = new Thread(new Execution());

    /**
     * Constructor which sets the width and height of the window and the column collection, it also works as a main method of the class, so it will show the window and run all the needed threads.
     * @param w window width
     * @param h window height
     * @param columns column collection which implements the List interface
     */
    public ColumnWindow(int w, int h) {
        super("Visualization of Sorting Algorithms");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvt) {
                sortingThread.stop();
            }
        });
        this.columns = getRndColumnArray(countArray[0]);
        sortingSleepTime = 2000 / columns.size();
        setPanels(w, h);
        setSortingAlgorithms();
        setCounts();
        this.setLayout(panels[0].getLayout());
      
            
          ImageIcon i = new ImageIcon("src/resources/backgee.jpg");
        JLabel l = new JLabel();
        l.setIcon(i);
        this.add(l);
    
        
        
        
        this.setSize(w + 10, h + 85);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(backgroundColor);
        panels[1].GetColumns().Sort(false);
        startRepaintThread();
        sortingThread.start();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/emgee.png");
        this.setIconImage(icon);
       
    }
    
    /**
     * Generates random column sequence.
     * @param count column count
     * @return generated sequence
     */
    private ArrayList<Column> getRndColumnArray(int count) {
        ArrayList<Column> columns = new ArrayList<Column>(0);
        for(int i = 0; i < count; i++)
            columns.add(new Column(rnd.nextInt(99) + 1));
        return columns;
    }
    
    
   
    
    
    /**
     * Adds all the sorting algorithms into the sortingAlgorithms combo box and sets the change value event for the combo box
     */
    private void setSortingAlgorithms() {
       
     setBackground(backgroundColor);
         
        saLabel.setVisible(true);
        this.add(saLabel);
        
        

       
        
        sortingAlgorithms.setVisible(true);
       
        
        sortingAlgorithms.addItem(new BubbleSort(sortingSleepTime));
        sortingAlgorithms.addItem(new InsertionSort(sortingSleepTime));
        sortingAlgorithms.addItem(new SelectionSort(sortingSleepTime));
        sortingAlgorithms.addItem(new ShellSort(sortingSleepTime));
        sortingAlgorithms.addItem(new QuickSort(sortingSleepTime));
        sortingAlgorithms.addItem(new MergeSort(sortingSleepTime));
        sortingAlgorithms.addItem(new HeapSort(sortingSleepTime));
        sortingAlgorithms.addItem(new CombSort(sortingSleepTime));
        sortingAlgorithms.addItem(new CocktailSort(sortingSleepTime));
        
        
        
        this.add(sortingAlgorithms);
        sortingAlgorithms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBackground(backgroundColor);
                sortingThread.stop();
                panels[1].GetColumns().SetColumns(columns);
                panels[1].GetColumns().SetSortingAlgorithm((SortingAlgorithm)sortingAlgorithms.getSelectedItem());
                panels[1].GetColumns().GetSortingAlgorithm().SetSleepTime(sortingSleepTime);
                sortingThread = new Thread(new Execution());
                sortingThread.start();
            }
        });
        panels[1].GetColumns().SetSortingAlgorithm((SortingAlgorithm)sortingAlgorithms.getSelectedItem());
       
    }
    
    /**
     * Adds all the required values into the counts combo box and sets change value event for that combo box
     */
    private void setCounts() {
        cLabel.setVisible(true);
        this.add(cLabel);
        counts.setVisible(true);
        for(int i = 0; i < countArray.length; i++)
            counts.addItem(countArray[i]);
        this.add(counts);
        counts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortingThread.stop();
                columns = getRndColumnArray((int)counts.getSelectedItem());
                for(int i = 0; i < panels.length; i++) {
                    panels[i].GetColumns().SetColumns(columns);
                    panels[i].repaint();
                }
                sortingSleepTime = 2000 / columns.size();
                panels[1].GetColumns().GetSortingAlgorithm().SetSleepTime(sortingSleepTime);
                sortingThread = new Thread(new Execution());
                sortingThread.start();
                setBackground(backgroundColor);
            }
        });
    }
    
    /**
     * Sets panels for drawing columns
     * @param w whole window width
     * @param h whole window height
     */
    private void setPanels(int w, int h) {
        panels[0] = new ColumnPanel("Unsorted State", columns);
        panels[1] = new ColumnPanel("Sorting Process", columns);
        for(int i = 0; i < panels.length; i++) {
            panels[i].setPreferredSize(new Dimension(w, h / panels.length));
            panels[i].setVisible(true);
            this.add(panels[i]);
            setBackground(backgroundColor);
        }
    }
    
    /**
     * Starts re-rendering thread of the third (bottom) panel, this thread will run until the window is not closed.
     */
    private void startRepaintThread() {
        Thread t = new Thread(new Runnable() {
           @Override
           public void run() {
               while(thisWindow.isVisible()) {
                   panels[1].repaint();
                   try {
                        Thread.sleep(0);
                   }
                   catch(Exception ex){}
               }
           }
        });
        t.start();
    }



    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setOpacity(0.0F);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

 
 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


