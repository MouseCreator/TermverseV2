package mouse.project.lib.web.dispatcher;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Prototype;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.context.WebContext;
import mouse.project.lib.web.exception.RequestProcessException;
import mouse.project.lib.web.mapper.URLTransform;
import mouse.project.lib.web.parse.JacksonBodyParser;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.request.RequestBody;
import mouse.project.lib.web.request.RequestBodyImpl;
import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.request.RequestURLImpl;
import mouse.project.lib.web.response.WebResponse;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
@Prototype
public class ReqRespContextImpl implements ReqRespContext {
    private final WebContext webContext;
    private final URLService urlService;
    private final JacksonBodyParser json;
    @Auto
    public ReqRespContextImpl(WebContext webContext, URLService urlService, JacksonBodyParser json) {
        this.webContext = webContext;
        this.urlService = urlService;
        this.json = json;
    }

    @Override
    public void useAndExecute(RequestMethod method, HttpServletRequest req, HttpServletResponse resp, Class<?> config) {
        try {
            processAndSend(method, req, resp, config);
        } catch (IOException e) {
            throw new RequestProcessException(e);
        }
    }

    private void processAndSend(RequestMethod method,
                                HttpServletRequest req,
                                HttpServletResponse resp,
                                Class<?> config) throws IOException {
        RequestURL requestURL = createRequest(req, method);

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

    private RequestURL createRequest(HttpServletRequest req, RequestMethod method) {
        RequestBody requestBody;
        try {
            String bodyString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            requestBody = new RequestBodyImpl(bodyString);
        } catch (IOException e) {
            throw new RequestProcessException(e);
        }
        String strUrl = URLTransform.getFullURL(req);
        FullURL fullURL = urlService.create(strUrl);
        return new RequestURLImpl(fullURL, method, requestBody);
    }


}
