package sorting;
import java.util.List;
public class HeapSort extends SortingAlgorithm {

    public HeapSort(int sleepTime) {
        super(sleepTime);
    }
    private void up(List<Column> columns, int i, boolean highlight) {
        boolean done = false;
        while(i > 0 && !done) {
            if(highlight) {
                columns.get(i).Highlight();
                columns.get((i - 1) / 2).Highlight();
                sleep();
            }
            if(columns.get((i - 1) / 2).GetValue() < columns.get(i).GetValue()) {
                Column temp = columns.get((i - 1) / 2);
                columns.set((i - 1) / 2, columns.get(i));
                columns.set(i, temp);
                if(highlight) sleep();
            }
            else
                done = true;
            if(highlight) {
                columns.get(i).Unhighlight();
                columns.get((i - 1) / 2).Unhighlight();
            }
            i = (i - 1) / 2;
        }
    }
    
    private void down(List<Column> columns, int last, boolean highlight) {
        int i = 0;
        boolean done = false;
        while(i  < last && !done) {
            if(highlight) {
                if(2 * i + 1 < last) columns.get(2 * i + 1).Highlight();
                if(2 * i + 2 < last) columns.get(2 * i + 2).Highlight();
                sleep();
            }
            int biggerSon = 2 * i + 2;
            if(biggerSon >= last || columns.get(2 * i + 1).GetValue() > columns.get(biggerSon).GetValue())
                biggerSon = 2 * i + 1;
            if(highlight) {
                if(2 * i + 1 < last) columns.get(2 * i + 1).Unhighlight();
                if(2 * i + 2 < last) columns.get(2 * i + 2).Unhighlight();
            }
            if(biggerSon >= last)
                break;
            if(highlight) {
                columns.get(i).Highlight();
                columns.get(biggerSon).Highlight();
                sleep();
            }
            if(columns.get(biggerSon).GetValue() > columns.get(i).GetValue()) {
                Column temp = columns.get(biggerSon);
                columns.set(biggerSon, columns.get(i));
                columns.set(i, temp);
                if(highlight) sleep();
            }
            else
                done = true;
            if(highlight) {
                columns.get(i).Unhighlight();
                columns.get(biggerSon).Unhighlight();
            }
            i = biggerSon;
        }
    }
    
  
    private void makeHeap(List<Column> columns, boolean highlight) {
        for(int i = 1; i < columns.size(); i++)
            up(columns, i, highlight);
    }
    
    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        makeHeap(columns, highlight);
        for(int last = columns.size() - 1; last >= 0; last--) {
            if(highlight) {
                columns.get(0).Highlight();
                columns.get(last).Highlight();
                sleep();
            }
            Column temp = columns.get(0);
            columns.set(0, columns.get(last));
            columns.set(last,  temp);
            sleep();
            if(highlight) {
                sleep();
                columns.get(0).Unhighlight();
                columns.get(last).Unhighlight();
            }
            down(columns, last, highlight);
        }
    }
    
    @Override
    public String toString() {
        return "Heap sort";
    }
}
