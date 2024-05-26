package mouse.project.lib.web.dispatcher;

import lombok.extern.log4j.Log4j2;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

@Service
@Prototype
@Log4j2
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
            log.error("Controller error: " + controllerException);
            log.error(ExceptionUtils.getStackTrace(controllerException));
            onError(resp, controllerException);
        } catch (StatusException statusException) {
            log.error("Status exception: " + statusException);
            log.error(ExceptionUtils.getStackTrace(statusException));
            errorHandlerInvoker.processError(statusException, resp);
        } catch (RuntimeException e) {
            log.error("Fatal error: " + e);
            log.error(ExceptionUtils.getStackTrace(e));
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
