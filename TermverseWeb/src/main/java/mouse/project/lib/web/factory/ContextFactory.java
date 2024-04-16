package mouse.project.lib.web.factory;

import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.EndpointContext;

import java.lang.reflect.Method;

public interface ContextFactory {
    ControllerContext getControllerContext(Object obj);
    EndpointContext getEndpointContext(ControllerContext parentContext, Method method);
}
