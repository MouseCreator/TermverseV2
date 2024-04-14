package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.helper.ParameterCreator;
import mouse.project.lib.ioc.injector.card.invoke.MethodInvoker;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.util.List;

public class MethodProducerImpl implements MethodProducer {
    private final MethodInvoker methodInvoker;
    public MethodProducerImpl(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }
    @Override
    public Object call(CardAccess container) {
        Implementation<?> origin = methodInvoker.getOrigin();
        Parameters parameters = methodInvoker.getParameters();
        ParameterCreator parameterCreator = new ParameterCreator(container);
        List<Object> args = parameterCreator.assignAll(parameters);
        Object obj = container.getImplementation(origin);
        return methodInvoker.invoke(obj, args);
    }
}
