package mouse.project.lib.web.context;

import mouse.project.lib.web.tool.FullURL;

import java.lang.reflect.Method;

public class EndpointContextImpl implements EndpointContext {
    private final Method method;
    private final FullURL urlTool;

    public EndpointContextImpl(Method method, FullURL urlTool) {
        this.method = method;
        this.urlTool = urlTool;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public FullURL getUrl() {
        return urlTool;
    }
}
