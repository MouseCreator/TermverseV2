package mouse.project.lib.data.page;

import java.util.ArrayList;
import java.util.List;

public class PageImpl<T> implements Page<T> {

    private final List<T> elements;
    private final PageDescription description;

    public PageImpl(List<T> elements, PageDescription description) {
        this.elements = elements;
        this.description = description;
    }

    @Override
    public PageDescription description() {
        return description;
    }

    @Override
    public int totalElements() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public List<T> getElements() {
        return new ArrayList<>(elements);
    }
}
