package mouse.project.lib.web.invoker;

import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.request.RequestURL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerInvokerImpl implements ControllerInvoker {
    private final Object controller;
    private final Method method;
    private final MethodArguments methodArguments;
    public ControllerInvokerImpl(Object controller,
                                 Method method,
                                 MethodArguments arguments) {
        this.controller = controller;
        this.method = method;
        this.methodArguments = arguments;
    }

    @Override
    public Object invoke(RequestURL requestURL) {
        Object[] res = getMethodParams(requestURL);
        method.setAccessible(true);
        try {
            return method.invoke(controller, res);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ControllerException("Error invoking method: ", e);
        }
    }
    private Object[] getMethodParams(RequestURL requestURL) {
        return methodArguments.getArguments(requestURL);
    }

}
