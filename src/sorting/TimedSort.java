package sorting;
import java.awt.*;
import java.util.Random;

class SortAbort extends RuntimeException {
}

final class TimedSort extends Canvas implements Runnable {

   int computeInterval = 400;  // time to compute between sleeps
   int pauseInterval = 100;    // time to sleep
   
   static int IDLE = 0, RUN = 2, ABORT = 3, ERROR = 4, APPLETSTOPPED = 5;
   int state = IDLE;
   int saveState = IDLE;
   String errorMessage;

   Color backgroundColor = new Color(204,204,255);
   Color borderColor = new Color(0,0,200);
   
   Color labelColor = borderColor;
   Color statsColor = Color.red;
   
   static String[] dataLabel =
                           { "Sorting Algorithm: ",
                             "Items per Array: ",
                             "Arrays Sorted: ",
                             "Elapsed Time: ",
                             "Approximate Compute Time: ",
                             "         ",
                             "         " };

   TimedSortPanel owner;
   LogPanel log;

   int[] A;  // array for sorting
   int[] B;  // extra array for Merge Sort

   Random rand = new Random();

   int sortMethod, arraySize, arrayCt;
      
   long comparisonCt, copyCt;
   int arraysSorted;
   long comparisonsSinceLastCheck;
   
   long startTime;
   long lastPauseTime;
   long totalPausedTime;
   
   
   long elapsedTimeForDisplay, computeTimeForDisplay;
   long comparisonCtForDisplay, copyCtForDisplay;
   int arraysSortedForDisplay;
   
   int width = -1, height;
   Font font;
   FontMetrics fm;
   
   Thread runner;
   
   
   TimedSort(TimedSortPanel owner, LogPanel log) {
      this.owner = owner;
      this.log = log;
      setBackground(backgroundColor);
      sortMethod = -1; // no method specified yet
   }
 
   @Override
   synchronized public void paint(Graphics g) {
      if (size().width != width || size().height != height || font == null) {
         width = size().width;
         height = size().height;
         font = new Font("Dialog",Font.PLAIN,18);
         fm = g.getFontMetrics(font);
         if (fm.stringWidth("Approximate Compute Time: ") > width/2 || fm.getHeight()*8 > height) {
             font = new Font("Dialog",Font.PLAIN,14);
             fm = g.getFontMetrics(font);
            
         }
      }
      g.setFont(font);
      g.setColor(Color.blue);
      g.drawRect(0,0,width,height);
      g.drawRect(1,1,width-2,height-2);
      g.drawLine(width-2,0,width-2,height);
      g.drawLine(0,height-2,width,height-2);
      int sw;
      if (state == ABORT) {
          sw = fm.stringWidth("OPERATION ABORTED.");
          g.setColor(statsColor);
          g.drawString("OPERATION ABORTED",(width-sw)/2,height/2);
      }
      else if (state == ERROR) {
          sw = fm.stringWidth(errorMessage);
          g.setColor(statsColor);
          g.drawString(errorMessage,(width-sw)/2,height/2);
      }
      else if (sortMethod == -1) {  // panel has not been used since it was shown -- give instructions
          g.setColor(labelColor);
          int x = fm.getHeight();
          g.drawString("Enter the array size and number of arrays.",x,2*x);
          g.drawString("Select a sorting algorithm.",x,3*x);
          g.drawString("Click \"Start Sorting\" to begin.",x,4*x);
      }
      else {  // report stats
          int ht = fm.getHeight();
          int y = (height - 7*ht + fm.getLeading())/2 + fm.getAscent();
          Rectangle clip = g.getClipRect();
          if (clip.x < width/2) {
             g.setColor(labelColor);
             for (int i = 0; i < 7; i++) {
                sw = fm.stringWidth(dataLabel[i]);
                g.drawString(dataLabel[i],width/2-sw,y+ht*i);
             }
             if (getState() == IDLE) {
                g.setColor(statsColor);
                g.drawString("SORTING HAS BEEN COMPLETED!",10,fm.getAscent()+10);
             }
          }
          if (clip.x + clip.width > width/2) {
             g.setColor(statsColor);
             int x = width/2 + 4;
             g.drawString(owner.sortName[sortMethod],x,y);
             g.drawString("" + arraySize,x,y+ht);
             g.drawString("" + arraysSortedForDisplay + " of " + arrayCt,x,y+2*ht);
             g.drawString("" + divideBy1000(elapsedTimeForDisplay) + " seconds",x,y+3*ht);
             g.drawString("" + divideBy1000(computeTimeForDisplay) + " seconds",x,y+4*ht);
            
          }
      }      
   }
   
   String divideBy1000(long n) {
      if (n == 0)
         return "0";
      long i = (n / 1000);
      long d = n - 1000*i;
      if (d > 99)
         return "" + i + '.' + d;
      else if (d > 9)
         return "" + i + ".0" + d;
      else
         return "" + i + ".00" + d;
   }
   
  
   synchronized void report(long elapsedTime, long computeTime, long comparisonCt, long copyCt, int arraysSorted) {
      elapsedTimeForDisplay = elapsedTime;
      computeTimeForDisplay = computeTime;
      comparisonCtForDisplay = comparisonCt;
      copyCtForDisplay = copyCt;
      arraysSortedForDisplay = arraysSorted;
   }
   
   synchronized void start(int sortMethod, int arraySize, int arrayCt) {
      if (state == RUN)
         return;
      state = RUN;
      this.sortMethod = sortMethod;
      this.arraySize = arraySize;
      this.arrayCt = arrayCt;
      report(0,0,0,0,0);
      repaint();
      if (runner == null || !runner.isAlive()) {
         runner = new Thread(this);
         runner.start();
      }
      notify();
   }
   
   synchronized void setState(int state) {
      this.state = state;
      notify();
   }
   
   synchronized int getState() {
      return state;
   }
   
   synchronized void setError(String message) {
      state = ERROR;
      errorMessage = message;
      repaint();
   }
   
   synchronized void doWait(int millis) {
      try { wait(millis); }
      catch (InterruptedException e) { }
   }
   
   boolean compare(boolean q) {
      comparisonCt++;
      comparisonsSinceLastCheck++;
      if (comparisonsSinceLastCheck == 10000) {
         comparisonsSinceLastCheck = 0;
         long time = System.currentTimeMillis();
         int st = getState();
         if (st == ABORT)
            throw new SortAbort();
         if (st == APPLETSTOPPED) {
            synchronized(this) {
               try { wait(); }
               catch (InterruptedException e) { }
            }
            long t = System.currentTimeMillis();
            totalPausedTime += (t - time);
            lastPauseTime = t;
         }
         else if (time - lastPauseTime >= computeInterval) {
            report(time - startTime, time - startTime - totalPausedTime, comparisonCt, copyCt, arraysSorted);
            repaint(size().width/2+1,4,size().width/2-5,size().height-8);
            doWait(pauseInterval);
            if (getState() == ABORT)
               throw new SortAbort();
            long endtime = System.currentTimeMillis();
            totalPausedTime += (endtime - time);
            lastPauseTime = endtime;
         }
      }
      return q;
   }
   
   void swap(int loc1, int loc2) { // swaps within array A
      int temp = A[loc1];
      A[loc1] = A[loc2];
      A[loc2] = temp;
      copyCt += 3;
   }
   
   void bubbleSort(int start, int end) {
      for (int top = end; top > start; top--)
         for (int i = start; i < top; i++)
            if (compare(A[i] > A[i+1]))
               swap(i, i+1);
   }
   
   void selectionSort(int start, int end) {
      for (int top = end; top > start; top--) {
         int max = start;
         for (int i = start+1; i <= top; i++)
            if (compare(A[i] > A[max]))
               max = i;
         swap(max,top);
      }
   }
   
   void insertionSort(int start, int end) {
      for (int insert = start+1; insert <= end; insert++) {
         int temp = A[insert];
         copyCt++;
         int i = insert-1;
         while (i >= start && compare(A[i] > temp)) {
            A[i+1] = A[i];
            copyCt++;
            i--;
         }
         A[i+1] = temp;
         copyCt++;
      }
   }
   
   void shellSort(int start, int end) {
       for(int gap = end/2; gap>0; gap/=2){
      for (int insert = start+1; insert < end; insert++) {
         int temp = A[insert];
         copyCt++;
         int i = gap-1;
         while (i >= start && compare(A[i] > temp)) {
            A[i+1] = A[i];
            copyCt++;
            i--;
         }
         A[i+1] = temp; 
         copyCt++;
      }
   }
   }
   
   
   
   void heapify(int A[],int n, int i) {
      int largest = i;
      int l = 2*i+1;
      int r = 2*i+2;
      
      if(l<n && A[l] > A[largest])
          largest = l;
      if(r<n && A[r] > A[largest])
          largest = r;
      if(largest!=i){
          int swap = A[i];
          
          A[i] = A[largest];
          copyCt++;
          A[largest] = swap;
          copyCt++;
          heapify(A, n, largest);
      }
   }
      
      
   
   void heapSort(int start, int end) {
     int n = A.length;
     int i = start;
     
     if(A.length>800){
     try{Thread.sleep(22);
     }
     catch(InterruptedException e){
       }   }
     for(i = n/2-1;i>=0;i--)
         heapify(A,n,i);
     for (i = n-1; i>=0; i--){
     int temp = A[0];
      copyCt++;
     A[0] = A[i];
      
     A[i] = temp;
     
    
     heapify(A, i, 0);
     }
   }
  

 
 int getNextGap(int gap){
     gap = (gap*10)/13;
     if(gap<1)
         return 1;
     return gap;
     }
 
 
 void combSort(int A[]){
     int n = A.length;
     int gap = n;
     boolean swapped = true;
     while(gap!=1 || swapped == true){
     gap = getNextGap(gap);
     swapped = false;
     for(int i = 0;i<n-gap;i++){
         if(A[i]>A[i+gap]){
             int temp = A[i+1];
             A[i] = A[i+gap];
             A[i+gap]=temp;
             swapped = true;
         }
     }
     }
 }

   
    void cocktailSort(int start, int end, int A[]) {
        boolean swapped = true;
        start = 0;
        end = A.length;
        
        while (swapped == true){
            swapped = false;
            
            for (int i = start; i < end - 1;++i){
                if (A[i] > A[i + 1] ){
                    int temp = A[i];
                    A[i] = A[i + 1];
                    A[i + 1] = temp;
                    swapped = true;
                }
            }
            
            if (swapped == false)
                break;
            swapped = false;
            end = end - 1;
            
            for (int i = end - 1; i >=start; i--){
            if (A[i] > A[i + 1]){
            int temp = A[i];
            A[i] = A[i + 1];
            A[i + 1] = temp;
            swapped = true;
            }
            }
            }
        }
    
   
   
   
   
   
   
      
   void doMerge(int from1, int to1, int from2, int to2, int count, int posInB) {
      for (int i = 0; i < count; i++) {
        if (from2 > to2)
           B[posInB++] = A[from1++];
        else if (from1 > to1)
           B[posInB++] = A[from2++];
        else if (compare(A[from1] < A[from2]))
           B[posInB++] = A[from1++];
        else
           B[posInB++] = A[from2++];
        copyCt++;
      }
   }
   
   void mergeSort(int start, int end) {
      int length = end - start + 1;
      int sortLength = 1;
      while (sortLength < length) {
         int from1 = start;
         while (from1 <= end) {
            int from2 = from1 + sortLength;
            int to1 = from2 - 1;
            int to2 = from2 + sortLength - 1;
            if (to1 >= end)
               doMerge(from1,end,0,-1,end-from1+1,from1-start);
            else if (to2 >= end)
               doMerge(from1,to1,from2,end,end-from1+1,from1-start);
            else
               doMerge(from1,to1,from2,to2,2*sortLength,from1-start);
            from1 = to2 + 1;
         }
         for (int i = 0; i < length; i++)
            A[start+i] = B[i];
         copyCt += length;
         sortLength *= 2;
      }
   }
   
   int quickSortStep(int lo, int hi) {
      int temp = A[hi];
      copyCt++;
      while (hi > lo) {
         while (hi > lo && compare(A[lo] <= temp))
           lo++;
         if (hi > lo) {
            A[hi] = A[lo];
            copyCt++;
            hi--;
            while (hi > lo && compare(A[hi] >= temp))
               hi--;
            if (hi > lo) {
               A[lo] = A[hi];
               copyCt++;
               lo++;
            }
         }
      }
      A[hi] = temp;
      copyCt++;
      return hi;
   }
   
   void quickSort(int start, int end) {
      if (end > start) {
         int mid = quickSortStep(start, end);
         if (mid - start > end - mid) {
            quickSort(mid+1,end);
            quickSort(start,mid-1);
         }
         else {
            quickSort(start,mid-1);
            quickSort(mid+1,end);
         }
      }
   }
   
   void doSort() {
      int size = arraySize*arrayCt;
      A = new int[size];
      if (sortMethod == 3)
         B = new int[arraySize];
      for (int i = 0; i < size; i++)
         A[i] = Math.abs(rand.nextInt());
      System.gc();
      owner.readyToStart();
      doWait(100);
      if (getState() == ABORT)
         throw new SortAbort();
      comparisonsSinceLastCheck = 0;
      copyCt = 0;
      comparisonCt = 0;
      totalPausedTime = 0;
      startTime = System.currentTimeMillis();
      lastPauseTime = startTime;
      arraysSorted = 0;
      int start = 0;
      int end = arraySize - 1;
      switch (sortMethod) {
         case 0:
            while (start < size) {
               bubbleSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
         case 1:
            while (start < size) {
               selectionSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
         case 2:
            while (start < size) {
               insertionSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
         case 3:
            while (start < size) {
               mergeSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
         case 4:
            while (start < size) {
               quickSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
             case 5:
            while (start < size) {
               shellSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
             case 6:
            while (start < size) {
               heapSort(start,end);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
             case 7:
            while (start < size) {
               combSort(A);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
             case 8:
            while (start < size) {
               cocktailSort(start,end,A);
               start = end + 1;
               end += arraySize;
               arraysSorted++;
            }
            break;
      }
      long time = System.currentTimeMillis();
      report(time - startTime, time - startTime - totalPausedTime, comparisonCt, copyCt, arraysSorted);
      if (arrayCt == 1)
         log.addLine(owner.sortName[sortMethod] + " applied to 1 array containing " + arraySize + " items:");
      else
         log.addLine(owner.sortName[sortMethod] + " applied to " + arrayCt + " arrays, each containing " + arraySize + " items:");
      log.addLine("   Elapsed Time: " + divideBy1000(time - startTime) + " seconds");
      log.addLine("   Approximate Compute Time: " + divideBy1000(time - startTime - totalPausedTime) + " seconds");
     
      log.addEoln();
      log.setBackground(Color.white);
   }

   @Override
   public void run() {
      while (true) {
         synchronized(this) {
            while (state != RUN) {
               try { wait(); }
               catch (InterruptedException e) { }
            }
            try { wait(100); }
            catch (InterruptedException e) { }
            if (state == ABORT) {
               repaint();
               owner.doneRunning();
               continue;
            }
         }
         try {
            doSort();
            setState(IDLE);
            repaint();
         }
         catch (SortAbort e) {
            repaint();
         }
         catch (OutOfMemoryError e) {
            setError("Not Enough Memory; use smaller or fewer arrays.");
         }
         catch (RuntimeException e) {
            setError("Sorry, an unexpected error occurred!");
         }
         A = B = null;
         owner.doneRunning();
      }
   }

}
