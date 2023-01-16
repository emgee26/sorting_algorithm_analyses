package sorting;
import java.util.List;

public class SelectionSort extends SortingAlgorithm {
   
    public SelectionSort(int sleepTime) {
        super(sleepTime);
    }
    
    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        for(int i = 0; i < columns.size(); i++) {
            if(highlight) {
                columns.get(i).Highlight();
                sleep();
            }
            int minIndex = i;
            for(int j = i; j < columns.size(); j++) {
                if(highlight) {
                    columns.get(j).Highlight();
                    sleep();
                }
                if(columns.get(j).GetValue() < columns.get(minIndex).GetValue()) {
                    if(highlight && minIndex != i) columns.get(minIndex).Unhighlight();
                    minIndex = j;
                }
                else if(highlight && j != i) columns.get(j).Unhighlight();
            }
            if(highlight) {
                columns.get(minIndex).Highlight();
                sleep();
            }
            Column temp = columns.get(minIndex);
            columns.set(minIndex, columns.get(i));
            columns.set(i, temp);
            if(highlight) {
                sleep();
                columns.get(minIndex).Unhighlight();
                columns.get(i).Unhighlight();
            }
        }
    }
    
    @Override
    public String toString() {
        return "Selection sort";
    }
}
