package mouse.project.lib.web.dispatcher;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Prototype;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.exception.NotFoundException;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLPath;
import mouse.project.lib.web.tool.URLPathNode;
import mouse.project.lib.web.tool.URLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Prototype
public class DispatcherMapImpl implements DispatcherMap {

    private final URLService urlService;
    private final Map<RequestMethod, MapNode> roots;
    private static final Logger logger = LogManager.getLogger(DispatcherMapImpl.class);
    @Auto
    public DispatcherMapImpl(URLService urlService) {
        this.urlService = urlService;
        roots = new HashMap<>();
        RequestMethod.all().forEach(r -> roots.put(r, new MapNode(urlService)));
    }

    public void setInvoker(String url, RequestMethod method, ControllerInvoker invoker) {
        FullURL fullURL = urlService.create(url);
        setInvoker(fullURL, method, invoker);
    }

    @Override
    public void setInvoker(FullURL fullURL, RequestMethod method, ControllerInvoker invoker) {
        URLPath path = fullURL.path();
        MapNode current = roots.get(method);
        if (current == null) {
            throw new ControllerException("Unknown method: " + method);
        }
        for (URLPathNode node : path.getNodes()) {
            String content = node.content();
            current = current.moveTo(content);
        }
        current.set(invoker, urlService.write(fullURL));
        logger.debug("Set " + method.name() + " " + urlService.write(fullURL) + " to " + invoker);
    }
    @Override
    public ControllerInvoker getInvoker(String url, RequestMethod method) {
        FullURL fullURL = urlService.create(url);
        return getInvoker(fullURL, method);
    }

    @Override
    public ControllerInvoker getInvoker(FullURL url, RequestMethod method) {
        URLPath path = url.path();
        MapNode current = roots.get(method);
        StringBuilder urlBuild = new StringBuilder();
        MapNode lastStar = null;

        for (URLPathNode node : path.getNodes()) {
            String content = node.content();
            urlBuild.append(content).append("/");
            Optional<MapNode> star = current.moveSafe("*");
            if (star.isPresent()) {
                lastStar = star.get();
            }
            Optional<MapNode> mapNode = current.moveSafe(content);
            if (mapNode.isEmpty()) {
                if (lastStar != null) {
                    return getStar(url, lastStar);
                }
                throw new NotFoundException("No path defined: " + urlBuild);
            }
            current = mapNode.get();
        }

        Optional<ControllerInvoker> invoker = current.getInvoker();
        if (invoker.isPresent()) {
            return invoker.get();
        }
        if (lastStar != null) {
            return getStar(url, lastStar);
        }
        throw new NotFoundException("No invoker defined for url: " + url);

    }

    @NotNull
    private static ControllerInvoker getStar(FullURL url, MapNode lastStar) {
        Optional<ControllerInvoker> optInvoker = lastStar.getInvoker();
        if (optInvoker.isEmpty()) {
            throw new NotFoundException("No invoker defined for url: " + url);
        }
        return optInvoker.get();
    }

    private static class MapNode {
        private final Map<String, MapNode> map;
        private final static String ARGUMENT_KEY = "[]";
        private final URLService service;
        public MapNode(URLService service) {
            this.service = service;
            map = new HashMap<>();
        }

        private ControllerInvoker innerInvoker = null;

        public MapNode moveTo(String str) {
            if (service.isArgument(str)) {
                str = ARGUMENT_KEY;
            }
            return map.computeIfAbsent(str, s -> new MapNode(service));
        }

        public Optional<MapNode> moveSafe(String str) {
            MapNode node = map.get(str);
            if (node == null) {
                node = map.get(ARGUMENT_KEY);
            }
            return Optional.ofNullable(node);
        }
        public void set(ControllerInvoker invoker, String url) {
            if (innerInvoker != null) {
                throw new ControllerException("Already defined listener for given url: " + url);
            }
            innerInvoker = invoker;
        }
        public void free() {
            innerInvoker = null;
        }

        public Optional<ControllerInvoker> getInvoker() {
            return Optional.ofNullable(innerInvoker);
        }
    }

}
