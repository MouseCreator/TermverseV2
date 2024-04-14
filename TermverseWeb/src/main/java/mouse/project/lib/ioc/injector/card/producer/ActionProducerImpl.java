package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.helper.ParameterCreator;
import mouse.project.lib.ioc.injector.card.invoke.ActionInvoker;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.util.List;

public class ActionProducerImpl implements ActionProducer {

    private final ActionInvoker actionInvoker;
    public ActionProducerImpl(ActionInvoker actionInvoker) {
        this.actionInvoker = actionInvoker;
    }

    @Override
    public void call(Object callOn, CardAccess container) {
        Parameters parameters = actionInvoker.getParameters();
        ParameterCreator parameterCreator = new ParameterCreator(container);
        List<Object> args = parameterCreator.assignAll(parameters);
        actionInvoker.invoke(callOn, args);
    }
}
