package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.helper.ParameterCreator;
import mouse.project.lib.ioc.injector.card.invoke.MethodInvoker;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.util.List;

public class SetterProducerImpl implements SetterProducer {
    private final MethodInvoker methodInvoker;
    public SetterProducerImpl(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    @Override
    public void apply(Object applyTo, CardAccess container) {
        Parameters parameters = methodInvoker.getParameters();
        ParameterCreator parameterCreator = new ParameterCreator(container);
        List<Object> args = parameterCreator.assignAll(parameters);
        methodInvoker.invoke(applyTo, args);
    }
}
