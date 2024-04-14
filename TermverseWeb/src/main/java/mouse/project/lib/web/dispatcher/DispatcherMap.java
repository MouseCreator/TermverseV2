package mouse.project.lib.web.dispatcher;

import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

public interface DispatcherMap {
    void setInvoker(String url, RequestMethod method, ControllerInvoker invoker);
    void setInvoker(FullURL url, RequestMethod method, ControllerInvoker invoker);
    ControllerInvoker getInvoker(String url, RequestMethod method);
    ControllerInvoker getInvoker(FullURL url, RequestMethod method);
}
