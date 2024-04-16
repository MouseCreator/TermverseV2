package mouse.project.lib.web.scan;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.ScanUtils;
import mouse.project.lib.web.annotation.RequestPrefix;
import mouse.project.lib.web.annotation.URL;
import mouse.project.lib.web.context.ControllerContext;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.factory.ContextFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Service
public class ControllerScanImpl implements ControllerScan {
    @Auto
    @Collect(WebAnnotationProcessor.class)
    private List<WebAnnotationProcessor> annotationProcessors;

    private final ContextFactory contextFactory;
    @Auto
    public ControllerScanImpl(ContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    public Collection<Registration> scanControllers(Collection<Object> allControllers) {
        List<Registration> registrations = new ArrayList<>();
        for (Object controller : allControllers) {
            Collection<Registration> newRegs = processController(controller);
            registrations.addAll(newRegs);
        }
        return registrations;
    }
    public Collection<Registration> scanController(Object controller) {
        return processController(controller);
    }
    private Collection<Registration> processController(Object controller) {
        Class<?> clazz = controller.getClass();
        List<Registration> result = new ArrayList<>();
        ScanUtils scanUtils = ScanUtils.getInstance();
        Collection<Method> methods = scanUtils.getAnnotatedMethod(clazz, URL.class);
        ControllerContext controllerContext = contextFactory.getControllerContext(controller);
        for (Method method : methods) {
            Collection<Registration> regs = getInvoker(controllerContext, method);
            if (regs.isEmpty()) {
                throw new ControllerException("Method " + method + " has @URL annotation, but cannot be processed." +
                        " Make sure at least one of CRUD annotations is present.");
            }
            result.addAll(regs);
        }
        return result;
    }

    private String getPrefix(Class<?> clazz) {
        RequestPrefix prefixAnnotation = clazz.getAnnotation(RequestPrefix.class);
        String prefix = "/";
        if (prefixAnnotation != null) {
            prefix += prefixAnnotation.value();
        }
        return prefix;
    }

    private String buildFullURL(Method method, String prefix) {
        String fullUrl = prefix + "/" + method.getAnnotation(URL.class).value() + "/";
        fullUrl = fullUrl.replaceAll("/+", "/");
        return fullUrl;
    }

    private Collection<Registration> getInvoker(ControllerContext controllerContext, Method method) {
        List<Registration> registrations = new ArrayList<>();
        for (WebAnnotationProcessor processor : annotationProcessors) {
            if (processor.canProcess(method)) {
                registrations.add(processor.process(controllerContext, method));
            }
        }
        return registrations;
    }
}
