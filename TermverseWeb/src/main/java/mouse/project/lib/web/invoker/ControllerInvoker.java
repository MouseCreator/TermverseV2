package mouse.project.lib.web.invoker;

import mouse.project.lib.web.request.RequestURL;


public interface ControllerInvoker {
    Object invoke(RequestURL requestURL);
}
