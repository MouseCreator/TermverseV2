package mouse.project.lib.web.context;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.dispatcher.WebDispatcher;
import mouse.project.lib.web.exception.ControllerException;

import java.util.HashMap;
import java.util.Map;
@Service
public class WebContextImpl implements WebContext {
    private final Map<Class<?>, WebDispatcher> dispatcherMap;

    public WebContextImpl() {
        this.dispatcherMap = new HashMap<>();
    }

    @Override
    public WebDispatcher getDispatcher(Class<?> configuration) {
        WebDispatcher webDispatcher = dispatcherMap.get(configuration);
        if (webDispatcher == null) {
            throw new ControllerException("No web dispatcher defined for given configuration: " + configuration);
        }
        return webDispatcher;
    }

    @Override
    public void setDispatcher(Class<?> configuration, WebDispatcher dispatcher) {
        dispatcherMap.put(configuration, dispatcher);
    }
}
