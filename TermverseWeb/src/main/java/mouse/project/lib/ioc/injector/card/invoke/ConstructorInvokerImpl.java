package mouse.project.lib.ioc.injector.card.invoke;

import mouse.project.lib.exception.CardException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConstructorInvokerImpl<T> implements ConstructorInvoker<T> {
    private final Constructor<T> constructor;
    private final Parameters parameters;
    public ConstructorInvokerImpl(Constructor<T> constructor, Parameters parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
    }

    @Override
    public T invoke(List<Object> arguments) {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(arguments.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CardException("Error calling constructor.", e);
        }
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }
}
