package mouse.project.lib.web.factory;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.invoker.ControllerInvokerImpl;
import mouse.project.lib.web.invoker.MethodArguments;
import mouse.project.lib.web.invoker.MethodArgumentsImpl;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ControllerInvokerFactoryImpl implements ControllerInvokerFactory {

    private final ArgumentFactory argumentFactory;
    private final ContextFactory contextFactory;
    @Auto
    public ControllerInvokerFactoryImpl(ArgumentFactory argumentFactory, ContextFactory contextFactory) {
        this.argumentFactory = argumentFactory;
        this.contextFactory = contextFactory;
    }

    @Override
    public ControllerInvoker create(ControllerContext context, Method method) {
        EndpointContext endpointContext = contextFactory.getEndpointContext(context, method);
        Parameter[] parameters = method.getParameters();
        MethodArguments methodArguments = createMethodArguments(parameters, endpointContext);
        return new ControllerInvokerImpl(context.getInstance(), method, methodArguments);
    }

    private MethodArguments createMethodArguments(Parameter[] parameters, EndpointContext endpointContext) {
        List<ArgumentDesc> descList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            ArgumentDesc argument = argumentFactory.createArgument(endpointContext, parameter);
            descList.add(argument);
        }
        return new MethodArgumentsImpl(descList);
    }
}
