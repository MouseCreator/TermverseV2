package mouse.project.lib.web.filter;

import java.util.List;

public class FilterChainImpl implements FilterChain {
    private int current = 0;
    private List<Filter> filterList;
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
