package mouse.project.lib.web.scan;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.Get;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.factory.ContextFactory;
import mouse.project.lib.web.factory.ControllerInvokerFactory;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;

import java.lang.reflect.Method;
@Service
public class GetAnnotationProcessor implements WebAnnotationProcessor {

    private final ControllerInvokerFactory controllerInvokerFactory;
    private final ContextFactory contextFactory;
    @Auto
    public GetAnnotationProcessor(ControllerInvokerFactory controllerInvokerFactory, ContextFactory contextFactory) {
        this.controllerInvokerFactory = controllerInvokerFactory;
        this.contextFactory = contextFactory;
    }

    @Override
    public boolean canProcess(Method method) {
        return method.isAnnotationPresent(Get.class);
    }

    @Override
    public Registration process(ControllerContext controller, Method method) {
        ControllerInvoker invoker = controllerInvokerFactory.create(controller, method);
        EndpointContext endpointContext = contextFactory.getEndpointContext(controller, method);
        return new Registration(endpointContext.getUrl(), RequestMethod.GET, invoker);
    }
}
