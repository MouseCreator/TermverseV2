package mouse.project.lib.web.factory;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.RequestPrefix;
import mouse.project.lib.web.annotation.URL;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.ControllerContextImpl;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.context.EndpointContextImpl;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLService;

import java.lang.reflect.Method;
@Service
public class ContextFactoryImpl implements ContextFactory {
    private final URLService urlService;
    @Auto
    public ContextFactoryImpl(URLService urlService) {
        this.urlService = urlService;
    }

    @Override
    public ControllerContext getControllerContext(Object obj) {
        FullURL urlTool = createURL(obj.getClass());
        return new ControllerContextImpl(obj, urlTool);
    }

    private FullURL createURL(Class<?> rootClass) {
        RequestPrefix annotation = rootClass.getAnnotation(RequestPrefix.class);
        String value = "/";
        if (annotation != null) {
            value = annotation.value();
        }
        return urlService.create(value);
    }

    @Override
    public EndpointContext getEndpointContext(ControllerContext parentContext, Method method) {
        FullURL url = parentContext.getUrl();
        URL methodURL = method.getAnnotation(URL.class);
        if (methodURL == null) {
            throw new ControllerException("Method " + method + " has no @URL annotation");
        }
        String value = methodURL.value();
        FullURL fullURL = urlService.extend(url, value);
        return new EndpointContextImpl(method, fullURL);
    }
}
