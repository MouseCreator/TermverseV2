package mouse.project.lib.web.scan;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.Post;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.factory.ContextFactory;
import mouse.project.lib.web.factory.ControllerInvokerFactory;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;

import java.lang.reflect.Method;
@Service
public class PostAnnotationProcessor implements WebAnnotationProcessor {
    private final ControllerInvokerFactory factory;
    private final ContextFactory contextFactory;
    @Auto
    public PostAnnotationProcessor(ControllerInvokerFactory factory, ContextFactory contextFactory) {
        this.factory = factory;
        this.contextFactory = contextFactory;
    }

    @Override
    public boolean canProcess(Method method) {
        return method.isAnnotationPresent(Post.class);
    }

    @Override
    public Registration process(ControllerContext controller, Method method) {
        ControllerInvoker invoker = factory.create(controller, method);
        EndpointContext endpointContext = contextFactory.getEndpointContext(controller, method);
        return new Registration(endpointContext.getUrl(), RequestMethod.POST, invoker);
    }
}
