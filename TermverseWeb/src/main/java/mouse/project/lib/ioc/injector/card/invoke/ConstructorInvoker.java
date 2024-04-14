package mouse.project.lib.ioc.injector.card.invoke;

import java.util.List;

public interface ConstructorInvoker<T> {
    T invoke(List<Object> parameters);
    Parameters getParameters();
}
