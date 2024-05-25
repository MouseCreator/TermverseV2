package mouse.project.lib.data.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortOrderImpl<T> implements SortOrder<T> {
    private final Comparator<T> comparator;
    public SortOrderImpl(Comparator<T> sortOrder) {
        this.comparator = sortOrder;
    }

    public void sort(List<T> list) {
        list.sort(comparator);
    }
    public Comparator<T> comparator() {
        return comparator;
    }
}
