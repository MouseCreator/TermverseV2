package mouse.project.lib.web.filter;

import java.util.List;

public class FilterChainImpl implements FilterChain {
    private int current;
    private final List<Filter> filterList;

    public FilterChainImpl(List<Filter> filterList) {
        this.filterList = filterList;
        current = 0;
    }

    @Override
    public void invokeNext() {
        if (hasNext()) {
            Filter filter = filterList.get(current++);
            filter.invoke();
        }
    }

    @Override
    public Filter getNext() {
        return filterList.get(current + 1);
    }

    @Override
    public boolean hasNext() {
        return current < filterList.size();
    }
}
