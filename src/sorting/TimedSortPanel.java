package sorting;
import java.awt.*;
import javax.swing.JPanel;

class TimedSortPanel extends JPanel {

  String[] sortName = { "Bubble Sort", "Selection Sort",
      "Insertion Sort", "Merge Sort", "Quick Sort", "Shell Sort", "Heap Sort",
                       "Comb Sort", "Cocktail Sort"};

   Choice methodChoice;
   Button goButton;
   TextField arrayCountInput;
   TextField arraySizeInput;
   boolean running;
   Color backgroundColor = new Color(204,204,255);

   TimedSort sorter;
   
   TimedSortPanel(LogPanel log) {
      setLayout(new BorderLayout(5,5));
      sorter = new TimedSort(this,log);
   
      Component add = add("Center",sorter);
     
       JPanel top = new JPanel();
       top.setBackground(backgroundColor);
      JPanel bottom = new JPanel();
      bottom.setBackground(backgroundColor);
      methodChoice = new Choice();
      for (String sortName1 : sortName) {
          methodChoice.addItem(sortName1);
      }
      bottom.add(methodChoice);
      goButton = new Button("Start Sorting");
      bottom.add(goButton);
      Component add1 = add("South",bottom);

      top.add(new Label("Array size:"));
      arraySizeInput = new TextField("10000",6);
      top.add(arraySizeInput);
      top.add(new Label("    Number of arrays:"));
      arrayCountInput = new TextField("1",6);
      top.add(arrayCountInput);
      Component add2 = add("North",top);
   }

   void aboutToShow() {
      sorter.sortMethod = -1;
      goButton.setLabel("Start Sorting");
      goButton.enable();
      arraySizeInput.requestFocus();
      running = false;
   }

   void aboutToHide() {
      if (sorter.getState() == TimedSort.RUN)
         sorter.setState(TimedSort.ABORT);
   }
   

   void doneRunning() {  // from sorter
      goButton.setLabel("Start Sorting");
      goButton.enable();
      arraySizeInput.requestFocus();
      running = false;
   }

   void readyToStart() { // called from sorter
      goButton.enable();
   }
   
   void startSorter() {
      int method = methodChoice.getSelectedIndex();
      int arrayCt = 0;
      int arraySize = 0;
      String str;
      str = arraySizeInput.getText();
      if (str == null || str.trim().length() == 0) {
         sorter.setError("Please enter an array size!");
         arraySizeInput.requestFocus();
         return;
      }
      try {
         arraySize = Integer.parseInt(str);
      }
      catch (NumberFormatException e) {
         sorter.setError("The array size must be an integer!");
         arraySizeInput.requestFocus();
         return;
      }
      if (arraySize <= 0) {
         if (arraySize == 0)
            sorter.setError("The array size can't be zero!");
         else
            sorter.setError("The array size can't be negative!");
         arraySizeInput.requestFocus();
         return;
      }
      str = arrayCountInput.getText();
      if (str == null || str.trim().length() == 0)
         arrayCt = 1;
      else {
        try {
           arrayCt = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
           sorter.setError("The number of arrays must be an integer!");
           arrayCountInput.requestFocus();
           return;
        }
        if (arrayCt <= 0) {
           if (arrayCt == 0)
              sorter.setError("The number of arrays cannot be zero!");
           else
              sorter.setError("The number of arrays cannot be negative!");
           arrayCountInput.requestFocus();
           return;
        }
      }
      if (arraySize > 10000000 || arrayCt > 10000000 || arraySize*(long)arrayCt > 10000000L) {
         sorter.setError("Not more than ten million items please!");
         arraySizeInput.requestFocus();
         return;
      }
      running = true;
      goButton.disable();
      goButton.setLabel("Abort");
      sorter.start(method,arraySize,arrayCt);

   }
   
  @Override
   public boolean action(Event evt, Object arg) {
      if (evt.target == goButton) {
         if (running) {
            goButton.disable();
            sorter.setState(TimedSort.ABORT);
         }
         else
            startSorter();
         return true;
      }
      else
         return super.action(evt,arg);
   }

}