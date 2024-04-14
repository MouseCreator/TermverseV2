package mouse.project.lib.web.scan;
import mouse.project.lib.web.context.ControllerContext;

import java.lang.reflect.Method;

public interface WebAnnotationProcessor {
    boolean canProcess(Method method);
    Registration process(ControllerContext controllerContext, Method method);
}
