package mouse.project.lib.web.dispatcher;

import mouse.project.lib.web.register.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ReqRespContext {
    void useAndExecute(RequestMethod method, HttpServletRequest req, HttpServletResponse resp, Class<?> config);
}
