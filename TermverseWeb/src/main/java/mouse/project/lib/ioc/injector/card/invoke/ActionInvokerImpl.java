package mouse.project.lib.ioc.injector.card.invoke;

import mouse.project.lib.exception.CardException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ActionInvokerImpl implements ActionInvoker {
    private final Method method;
    private final Parameters parameters;

    public ActionInvokerImpl(Method method, Parameters parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public Object invoke(Object invokeOn, List<Object> params) {
        method.setAccessible(true);
        try {
            return method.invoke(invokeOn, params.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            String message =
                    "Error invoking method. Cannot invoke " + method + " on object " + invokeOn + " with params " + params;
            throw new CardException(message, e);
        }
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }
}
