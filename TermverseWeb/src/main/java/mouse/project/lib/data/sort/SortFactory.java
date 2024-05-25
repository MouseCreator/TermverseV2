package mouse.project.lib.data.sort;

import java.util.Comparator;

public interface SortFactory {
    <T> SortOrder<T> createSortOrder(Comparator<T> sortOrder);
}
