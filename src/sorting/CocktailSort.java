package sorting;
import java.util.List;
public class CocktailSort extends SortingAlgorithm {
   
    public CocktailSort(int sleepTime) {
        super(sleepTime);
    }
    
    @Override
    public void Sort(List<Column> columns, boolean highlight) {
        boolean done = false;
        for(int last = columns.size(), first = 0; last > first && !done; last--, first++) {
            done = true;
            for(int j = first; j < last - 1; j++) {
                if(highlight) {
                    columns.get(j).Highlight();
                    columns.get(j + 1).Highlight();
                    sleep();
                }
                if(columns.get(j).GetValue() > columns.get(j + 1).GetValue()) {
                    Column temp = columns.get(j);
                    columns.set(j, columns.get(j + 1));
                    columns.set(j + 1,  temp);
                    done = false;
                    if(highlight) sleep();
                }
                if(highlight) {
                    columns.get(j).Unhighlight();
                    columns.get(j + 1).Unhighlight();
                }
            }
            done = true;
            for(int j = last - 2; j > first - 1; j--) {
                if(highlight) {
                    columns.get(j).Highlight();
                    columns.get(j + 1).Highlight();
                    sleep();
                }
                if(columns.get(j).GetValue() > columns.get(j + 1).GetValue()) {
                    Column temp = columns.get(j);
                    columns.set(j, columns.get(j + 1));
                    columns.set(j + 1,  temp);
                    done = false;
                    if(highlight) sleep();
                }
                if(highlight) {
                    columns.get(j).Unhighlight();
                    columns.get(j + 1).Unhighlight();
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Cocktail sort";
    }
}
