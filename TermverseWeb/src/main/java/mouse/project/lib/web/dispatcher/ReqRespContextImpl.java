package mouse.project.lib.web.dispatcher;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Prototype;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.context.WebContext;
import mouse.project.lib.web.exception.RequestProcessException;
import mouse.project.lib.web.parse.JacksonBodyParser;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.response.WebResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Prototype
public class ReqRespContextImpl implements ReqRespContext {
    private final WebContext webContext;
    private final RequestPreinitializer requestPreinitializer;
    private final JacksonBodyParser json;
    @Auto
    public ReqRespContextImpl(WebContext webContext,
                              RequestPreinitializer requestPreinitializer,
                              JacksonBodyParser json) {
        this.webContext = webContext;
        this.requestPreinitializer = requestPreinitializer;
        this.json = json;
    }

    @Override
    public void useAndExecute(RequestMethod method, HttpServletRequest req, HttpServletResponse resp, Class<?> config) {
        try {
            processAndSend(req, resp, config);
        } catch (IOException e) {
            throw new RequestProcessException(e);
        }
    }

    private void processAndSend(
                                HttpServletRequest req,
                                HttpServletResponse resp,
                                Class<?> config) throws IOException {
        RequestURL requestURL = requestPreinitializer.createRequest(req);

        WebDispatcher dispatcher = webContext.getDispatcher(config);
        WebResponse webResponse = dispatcher.onRequest(requestURL);
        Object result = webResponse.getResult();
        int status = webResponse.status();
        resp.setStatus(status);

        if (status >= 400) {
            resp.sendError(status);
            return;
        }

        String toWrite = json.unparse(result);
        resp.getWriter().write(toWrite);
        resp.flushBuffer();
    }




}
