package mouse.project.lib.data.sort;

import mouse.project.lib.ioc.annotation.Service;

import java.util.Comparator;
@Service
public class SortFactoryImpl implements SortFactory {
    @Override
    public <T> SortOrder<T> createSortOrder(Comparator<T> sortOrder) {
        return new SortOrderImpl<>(sortOrder);
    }
}
