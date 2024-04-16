package mouse.project.lib.web.setup;

import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.filter.CommonFilters;
import mouse.project.lib.web.context.WebContext;
import mouse.project.lib.web.dispatcher.DispatcherMap;
import mouse.project.lib.web.dispatcher.WebDispatcher;
import mouse.project.lib.web.scan.ControllerScan;
import mouse.project.lib.web.scan.Registration;

import java.util.Collection;
@Service
public class ControllersPrepare {

    private final ControllerScan controllerScan;
    @Auto
    public ControllersPrepare(ControllerScan controllerScan) {
        this.controllerScan = controllerScan;
    }

    public void prepareControllers(Class<?> configClass) {
        Inj inj = Ioc.getConfiguredInjector(configClass);
        Collection<Object> allControllers = CommonFilters.getAllAnnotatedWith(inj, Controller.class);
        Collection<Registration> registrations = controllerScan.scanControllers(allControllers);
        WebDispatcher webDispatcher = inj.get(WebDispatcher.class);
        DispatcherMap dispatcherMap = inj.get(DispatcherMap.class);
        WebContext webContext = inj.get(WebContext.class);

        registrations.forEach(r -> dispatcherMap.setInvoker(r.getUrl(), r.getRequestMethod(), r.getInvoker()));

        webDispatcher.useMap(dispatcherMap);
        webContext.setDispatcher(configClass, webDispatcher);
    }
}
