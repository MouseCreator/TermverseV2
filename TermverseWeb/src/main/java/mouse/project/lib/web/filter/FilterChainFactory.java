package mouse.project.lib.web.filter;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLPath;
import mouse.project.lib.web.tool.URLPathNode;
import mouse.project.lib.web.tool.URLService;

import java.util.List;

@Service
public class FilterChainFactory {
    private final @Collect(Filter.class) List<Filter> filterList;

    private final URLService urlService;
    @Auto
    public FilterChainFactory(List<Filter> filterList, URLService urlService) {
        this.filterList = filterList;
        this.urlService = urlService;
    }

    public FilterChain createChain(FullURL inputUrl) {
        URLPath path = inputUrl.path();
        List<Filter> list = filterList.stream().filter(f -> matches(path, f)).toList();
        return new FilterChainImpl(list);
    }

    private boolean matches(URLPath path, Filter filter) {
        return isAmongAccepted(path, filter) &&
                !isAmongRejected(path, filter);
    }

    private boolean isAmongAccepted(URLPath path, Filter filter) {
        List<String> acceptUrls = filter.alwaysApplyToUrls();
        return hasUrlMatch(path, acceptUrls);
    }
    private boolean isAmongRejected(URLPath path, Filter filter) {
        List<String> rejectUrls = filter.neverApplyToUrls();
        return hasUrlMatch(path, rejectUrls);
    }

    private boolean hasUrlMatch(URLPath path, List<String> rejectUrls) {
        for (String urlString : rejectUrls) {
            FullURL fullURL = urlService.create(urlString);
            if (matches(path, fullURL.path())) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(URLPath input, URLPath target) {
        List<URLPathNode> targetNodes = target.getNodes();
        if (targetNodes.isEmpty()) {
            return true;
        }
        URLPathNode last = targetNodes.get(targetNodes.size() - 1);
        if (isStarNode(last)) {
            return isPartialMatch(input, target);
        } else {
            return isExactMatch(input, target);
        }
    }

    private boolean isPartialMatch(URLPath input, URLPath target) {
        List<URLPathNode> inputNodes = input.getNodes();
        List<URLPathNode> targetNodes = target.getNodes();
        for (int i = 0; i < targetNodes.size() - 1; i++) {
            URLPathNode inputNode = inputNodes.get(0);
            URLPathNode targetNode = targetNodes.get(0);
            boolean match = inputNode.equals(targetNode);
            if (!match) {
                return false;
            }
        }
        return true;
    }

    private boolean isExactMatch(URLPath input, URLPath target) {
        return input.equals(target);
    }

    private boolean isStarNode(URLPathNode node) {
        return "*".equals(node.content());
    }
}
