package sorting;
import java.util.List;
/**
 * Bubble sort class.
 * @author Group 3 Project Work
 */
public class BubbleSort extends SortingAlgorithm {
    
   
            /**
     * Sets the time for the sleep by calling the parent constructor.
     * @param sleepTime the value of the sleep time
     */
    public BubbleSort(int sleepTime) {
        

        super(sleepTime);
        
    }
    
    
    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        
        
        for(int i = 0, last = columns.size(); i < columns.size(); i++, last--)
            for(int j = 0; j < last - 1; j++) {
                if(highlight) {
                    columns.get(j).Highlight();
                    columns.get(j + 1).Highlight();
                    sleep();
                }
                if(columns.get(j).GetValue() > columns.get(j + 1).GetValue()) {
                    Column temp = columns.get(j);
                    columns.set(j, columns.get(j + 1));
                    columns.set(j + 1,  temp);
                    if(highlight) sleep();
                }
                if(highlight) {
                    columns.get(j).Unhighlight();
                    columns.get(j + 1).Unhighlight();
        
      
                }
            }
    }
    
    @Override
    public String toString() {
        return "Bubble sort";}
         
    }
    
    

