package mouse.project.lib.web.scan;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.Update;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.factory.ContextFactory;
import mouse.project.lib.web.factory.ControllerInvokerFactory;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;

import java.lang.reflect.Method;
@Service
public class UpdateAnnotationProcessor implements WebAnnotationProcessor {

    private final ControllerInvokerFactory cif;

    private final ContextFactory contextFactory;
    @Auto
    public UpdateAnnotationProcessor(ControllerInvokerFactory cif, ContextFactory contextFactory) {
        this.cif = cif;
        this.contextFactory = contextFactory;
    }

    @Override
    public boolean canProcess(Method method) {
        return method.isAnnotationPresent(Update.class);
    }

    @Override
    public Registration process(ControllerContext controllerContext, Method method) {
        ControllerInvoker invoker = cif.create(controllerContext, method);
        EndpointContext endpointContext = contextFactory.getEndpointContext(controllerContext, method);
        return new Registration(endpointContext.getUrl(), RequestMethod.UPDATE, invoker);
    }
}
