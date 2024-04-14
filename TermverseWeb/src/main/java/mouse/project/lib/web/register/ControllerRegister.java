package mouse.project.lib.web.register;

import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.scan.Registration;

public interface ControllerRegister {
    void add(RequestMethod requestMethod, String fullUrl, ControllerInvoker invoker);
    void add(Registration registration);
}
