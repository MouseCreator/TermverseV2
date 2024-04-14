package mouse.project.lib.web.context;

import java.lang.reflect.Method;

public interface EndpointContext extends URLHolder {
    Method getMethod();
}
