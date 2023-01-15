package sorting;
import java.awt.*;
import javax.swing.JFrame;

class SortLabFrame extends JFrame {
  Color backgroundColor = new Color(204,204,255);
   public static void main(String[] args) {
      SortLabFrame frame = new SortLabFrame();   
      frame.setLocationRelativeTo(null);
       frame.getContentPane().setBackground(new Color(204,204,255));
      
   }
   
   public static void runNoExit() {
      SortLabFrame frame = new SortLabFrame();  
      
      frame.exitOnClose = false;
      
   }
   
   private final SortLabPanel sortPanel;
   private boolean closed = false;
   
   boolean exitOnClose = true;
   
    SortLabFrame() {
       
      super("Empirical Analysis");
      Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/emgee.png");
      this.setIconImage(icon);
      sortPanel = new SortLabPanel();
      this.setBackground(backgroundColor);
      add("Center",sortPanel);
      reshape(25,35,645,485);
      setResizable(false);
      setVisible(true);
      

       
   }
   
   
   boolean isClosed() {
      return closed;
   }
   
   void close() {
     
     sortPanel.destroy();
     closed = true;
     dispose();
     if (exitOnClose)
        System.exit(0);
   }
   
   @Override
   public boolean handleEvent(Event evt) {
      if (evt.id == Event.WINDOW_DESTROY) {
         close();
         return true;
      }
      else
         return super.handleEvent(evt);
   }

}
