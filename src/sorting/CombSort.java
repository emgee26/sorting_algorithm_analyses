package sorting;
import java.util.List;
public class CombSort extends SortingAlgorithm {
    
    public CombSort(int sleepTime) {
        super(sleepTime);
    }
    
    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        boolean swapped = true;
        double divider = 4/3.0;
        for (int space = (int)(columns.size() / divider); swapped || space > 1; space /= space > divider ? divider : 1) {
            swapped = false;
            for (int first = 0; first < columns.size() - space; first++) {
                if(highlight) {
                    columns.get(first).Highlight();
                    columns.get(first + space).Highlight();
                    sleep();
                }
                if(columns.get(first).GetValue() > columns.get(first + space).GetValue()) {
                    Column temp = columns.get(first);
                    columns.set(first, columns.get(first + space));
                    columns.set(first + space,  temp);
                    swapped = true;
                    if(highlight) sleep();
                }
                if(highlight) {
                    columns.get(first).Unhighlight();
                    columns.get(first + space).Unhighlight();
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Comb sort";
    }
}
