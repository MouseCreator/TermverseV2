package mouse.project.lib.ioc.injector.card.invoke;

import java.util.List;

public interface ActionInvoker {
    Object invoke(Object callOn, List<Object> parameters);
    Parameters getParameters();
}
