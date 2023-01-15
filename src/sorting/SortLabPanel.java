package sorting;
import java.awt.*;
import javax.swing.JPanel;

public class SortLabPanel extends JPanel {
      Color backgroundColor = new Color(204,204,255);
   Choice panelChoice;
   LogPanel log;
   TimedSortPanel timed;
   int currentPanel;
   
   JPanel mainPanel;
   CardLayout mainLayout;

   public SortLabPanel() {
      setBackground(backgroundColor);
      setLayout(new BorderLayout(5,5));
      setOpaque(true);
      panelChoice = new Choice();
     
      panelChoice.addItem("Timed Mode");
      panelChoice.addItem("Log Information");
      
      add("North",panelChoice);
      
      mainPanel = new JPanel();
      mainLayout = new CardLayout();
      mainPanel.setLayout(mainLayout);
      
      log = new LogPanel();

      timed = new TimedSortPanel(log);
      
  
      mainPanel.add("timed",timed);
      mainPanel.add("log",log);
      currentPanel = 1;
      mainPanel.setBackground(backgroundColor);
      add("Center",mainPanel);
   }
   
   
   public void destroy() {
      if (timed.sorter.runner != null && timed.sorter.runner.isAlive())
         timed.sorter.runner.stop();
      if (timed.sorter.runner != null && timed.sorter.runner.isAlive())
         timed.sorter.runner.stop();
   }

   public boolean action(Event evt, Object arg) {
      if (evt.target == panelChoice) {
         doPanelChoice();
         return true;
      }
      else
         return super.action(evt,arg);   
   }
   
   void doPanelChoice() {
     int item = panelChoice.getSelectedIndex();
     
     if (currentPanel == 0)
        timed.aboutToHide();
    
    
     if (item == 0) {
        timed.aboutToShow();
        mainLayout.show(mainPanel,"timed");
     }
    
     else {
        log.aboutToShow();
        mainLayout.show(mainPanel,"log");
        log.shown();
     }
     currentPanel = item;  
   }

}