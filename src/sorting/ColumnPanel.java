package sorting;
import java.awt.*;
import java.util.List;
import javax.swing.*;


public class ColumnPanel extends JPanel {
    private final String sign;
    private final ColumnCollection columns;
     Color backgroundColor = new Color(255,255,255);

    public ColumnPanel(String sign, List<Column> columns) {
        this.sign = sign;
        this.columns = new ColumnCollection(columns);   
         
  
    }

   
    private void drawBackground(int xBegin, int xEnd, int yBegin, int height, Graphics g) {
 
        g.setColor(Color.black);
        super.paintComponent(g);
        g.drawString(sign, 5, 15);
        for(int i = 10; i >= 0; i--) {
            int h = yBegin + (height / 11) * i;
            g.setColor(Color.blue);
            g.drawString(String.valueOf(100 - i * 10), xBegin + 3, h);
                g.setColor(Color.red);
            g.drawLine(xBegin, h + 1, xEnd, h + 1);
        }
        g.drawLine(xBegin, yBegin - 10, xBegin, yBegin + (height /11) * 10);
      setBackground(backgroundColor);
    }
    

    
    /**
     * Method for drawing the component (draws the background and columns)
     * @param g canvas
     */
    @Override
    public void paintComponent(Graphics g) {
        
        final int HEIGHT = this.getHeight() - 30;
        final int X_BEGIN = 5;
        final int X_END = this.getWidth() - 10;
        final int Y_BEGIN = 35;
        final int Y_END = Y_BEGIN + (HEIGHT / 11) * 10;
        drawBackground(X_BEGIN, X_END, Y_BEGIN, HEIGHT, g);
        columns.Draw(X_BEGIN + 30, X_END, Y_BEGIN, Y_END, true, 10, 10, g);
      setBackground(backgroundColor);
    
    }

    /**
     * Column collection getter
     * @return column collection in the object of type ColumnCollection
     */
    public ColumnCollection GetColumns() {
        return columns;
       
       
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}