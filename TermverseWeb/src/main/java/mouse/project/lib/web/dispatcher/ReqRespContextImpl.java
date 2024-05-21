package mouse.project.lib.web.dispatcher;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Prototype;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.context.WebContext;
import mouse.project.lib.web.error.ErrorHandlerInvoker;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.exception.RequestProcessException;
import mouse.project.lib.web.exception.StatusException;
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
    private final ErrorHandlerInvoker errorHandlerInvoker;
    @Auto
    public ReqRespContextImpl(WebContext webContext,
                              RequestPreinitializer requestPreinitializer,
                              JacksonBodyParser json,
                              ErrorHandlerInvoker errorHandlerInvoker) {
        this.webContext = webContext;
        this.requestPreinitializer = requestPreinitializer;
        this.json = json;
        this.errorHandlerInvoker = errorHandlerInvoker;
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
        try {
            WebResponse webResponse = dispatcher.onRequest(requestURL);
            onSuccess(resp, webResponse);
        } catch (ControllerException controllerException) {
            onError(resp, controllerException);
        } catch (StatusException statusException) {
            errorHandlerInvoker.processError(statusException, resp);
        } catch (RuntimeException e) {
            onFatalError(e, resp);
        }

    }

    private void onFatalError(RuntimeException e, HttpServletResponse response) {
        errorHandlerInvoker.onFatalError(e, response);
    }

    private void onError(HttpServletResponse resp, ControllerException controllerException) {
        Throwable cause = controllerException.getCause();
        if (cause instanceof RuntimeException) {
            errorHandlerInvoker.processError((RuntimeException) cause, resp);
        }
    }

    private void onSuccess(HttpServletResponse resp, WebResponse webResponse) throws IOException {
        Object result = webResponse.getResult();
        int status = webResponse.status();
        resp.setStatus(status);

        String toWrite = json.unparse(result);
        resp.getWriter().write(toWrite);
        resp.flushBuffer();
    }


}
