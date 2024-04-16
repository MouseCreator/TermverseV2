package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.helper.ParameterCreator;
import mouse.project.lib.ioc.injector.card.invoke.ConstructorInvoker;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.util.List;

public class ConstructorProducerImpl<T> implements ConstructorProducer<T> {
    private final ConstructorInvoker<T> invoker;

    public ConstructorProducerImpl(ConstructorInvoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public T construct(CardAccess container) {
        Parameters parameters = invoker.getParameters();
        ParameterCreator parameterCreator = new ParameterCreator(container);
        List<Object> args = parameterCreator.assignAll(parameters);
        return invoker.invoke(args);
    }
}
