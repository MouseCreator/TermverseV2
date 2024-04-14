package mouse.project.lib.ioc.injector.card.container;

import java.lang.reflect.Method;

public class Implementations {
    public static <T> Implementation<T> create(Class<T> tClass) {
        ImplementationCreator implementationCreator = new ImplementationCreatorImpl();
        return implementationCreator.getImplementation(tClass);
    }

    public static Implementation<?> create(Method method) {
        ImplementationCreator implementationCreator = new ImplementationCreatorImpl();
        return implementationCreator.getImplementation(method);
    }
}
