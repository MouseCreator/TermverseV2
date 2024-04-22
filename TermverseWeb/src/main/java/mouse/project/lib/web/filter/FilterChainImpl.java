package mouse.project.lib.web.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class FilterChainImpl implements FilterChain {
    private int current;
    private final List<MFilter> filterList;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    public FilterChainImpl(List<MFilter> filterList, HttpServletRequest request, HttpServletResponse response) {
        this.filterList = filterList;
        this.response = response;
        this.request = request;
        current = 0;
    }

    @Override
    public void invokeNext() {
        if (hasNext()) {
            MFilter filter = filterList.get(current++);
            try {
                filter.invoke(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public MFilter getNext() {
        return filterList.get(current + 1);
    }

    @Override
    public boolean hasNext() {
        return current < filterList.size();
    }
}
