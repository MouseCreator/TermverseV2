package mouse.project.lib.ioc.injector.card.invoke;


import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.util.List;

public interface MethodInvoker {
    Object invoke(Object invokeOn, List<Object> parameters);
    Parameters getParameters();
    Implementation<?> getOrigin();
}
