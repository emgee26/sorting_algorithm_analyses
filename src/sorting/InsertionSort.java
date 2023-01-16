package sorting;
import java.util.List;

public class InsertionSort extends SortingAlgorithm {
    public InsertionSort(int sleepTime) {
        super(sleepTime);
    }

    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        for(int i = 1; i < columns.size(); i++) {
            int moving = columns.get(i).GetValue();
            int pos = i;
            for(int j = i - 1; j >= 0; j--) {
                if(highlight) {
                    columns.get(j + 1).Highlight();
                    sleep();
                    columns.get(j + 1).Unhighlight();
                }
                if(moving < columns.get(j).GetValue()) {
                    pos = j;
                    columns.get(j + 1).SetValue(columns.get(j).GetValue());
                }
                else
                    break;
            }
            columns.get(pos).SetValue(moving);
            if(highlight) {
                columns.get(pos).Highlight();
                sleep();
                columns.get(pos).Unhighlight();
            }
        }
    }
    
    @Override
    public String toString() {
        return "Insertion sort";
    }
}
