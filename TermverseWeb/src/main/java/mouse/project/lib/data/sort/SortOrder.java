package mouse.project.lib.data.sort;

import java.util.Comparator;
import java.util.List;

public interface SortOrder<T> {
    void sort(List<T> list);
    Comparator<T> comparator();
}
