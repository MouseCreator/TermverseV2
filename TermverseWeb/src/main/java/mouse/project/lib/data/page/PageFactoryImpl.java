package mouse.project.lib.data.page;

import mouse.project.lib.data.sort.SortOrder;
import mouse.project.lib.ioc.annotation.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class PageFactoryImpl implements PageFactory {
    @Override
    public <T> Page<T> pageOf(List<T> elements, PageDescription description) {
        if (elements.size() > description.size()) {
            throw new IllegalArgumentException("Page cannot contain more elements than expected page size. Got: "
                    + elements.size() + ", limit: " + description.size());
        }
        return new PageImpl<>(new ArrayList<>(elements), description);
    }

    @Override
    public PageDescription pageDescription(Integer page, Integer size) {
        if (size < 0) {
            throw new IllegalArgumentException("Cannot create a page with negative size: " + size);
        }
        if (page < 0) {
            throw new IllegalArgumentException("Cannot create a page with negative number: " + page);
        }
        return new PageDescriptionImpl(page, size);
    }

    @Override
    public <T> Page<T> pageOf(List<T> list, PageDescription pageDescription, SortOrder<T> sortOrder) {
        List<T> toSort = new ArrayList<>(list);
        sortOrder.sort(toSort);
        return pageOf(toSort, pageDescription);
    }

    @Override
    public <T> Page<T> applyPageDescription(List<T> list, PageDescription pageDescription, SortOrder<T> sortOrder) {
        List<T> toSort = new ArrayList<>(list);
        sortOrder.sort(toSort);
        return applyPageDescription(toSort, pageDescription);
    }

    @Override
    public <T> Page<T> applyPageDescription(List<T> list, PageDescription pageDescription) {
        int size = pageDescription.size();
        int number = pageDescription.number();
        if (size < 0) {
            throw new IllegalArgumentException("Cannot create a page with negative size: " + size);
        }
        if (number < 0) {
            throw new IllegalArgumentException("Cannot create a page with negative number: " + number);
        }
        int fromIndex = number * size;
        int toIndex = (number + 1) * size;
        if (fromIndex >= list.size()) {
            return pageOf(List.of(), pageDescription);
        }
        if (toIndex >= list.size()) {
            toIndex = list.size();
        }
        List<T> sublist = list.subList(fromIndex, toIndex);
        return pageOf(sublist, pageDescription);
    }
}
