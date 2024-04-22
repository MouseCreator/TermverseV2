package mouse.project.lib.web.filter;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;

import java.util.List;

@Service
public class FilterChainFactory {
    private final @Collect(Filter.class) List<Filter> filterList;
    @Auto
    public FilterChainFactory(List<Filter> filterList) {
        this.filterList = filterList;
    }

    public FilterChain createChain() {
        return new FilterChainImpl(filterList);
    }
}
