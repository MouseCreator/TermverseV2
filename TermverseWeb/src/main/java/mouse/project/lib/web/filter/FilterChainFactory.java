package mouse.project.lib.web.filter;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.dispatcher.RequestPreinitializer;
import mouse.project.lib.web.tool.*;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class FilterChainFactory {
    private final @Collect(MFilter.class) List<MFilter> filterList;
    private final RequestPreinitializer requestPreinitializer;
    private final URLService urlService;
    @Auto
    public FilterChainFactory(@Collect(MFilter.class) List<MFilter> filterList,
                              RequestPreinitializer requestPreinitializer,
                              URLService urlService) {
        this.filterList = filterList;
        this.requestPreinitializer = requestPreinitializer;
        this.urlService = urlService;
    }

    public FilterChain createChain(ServletRequest request, ServletResponse response) {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        FullURL inputUrl = requestPreinitializer.getRequestUrl(httpRequest);
        URLPath path = inputUrl.path();
        List<MFilter> list = filterList.stream().filter(f -> matches(path, f)).toList();
        return new FilterChainImpl(list, httpRequest, httpResponse);
    }

    private boolean matches(URLPath path, MFilter filter) {
        return isAmongAccepted(path, filter) &&
                !isAmongRejected(path, filter);
    }

    private boolean isAmongAccepted(URLPath path, MFilter filter) {
        List<String> acceptUrls = filter.alwaysApplyToUrls();
        return hasUrlMatch(path, acceptUrls);
    }
    private boolean isAmongRejected(URLPath path, MFilter filter) {
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
