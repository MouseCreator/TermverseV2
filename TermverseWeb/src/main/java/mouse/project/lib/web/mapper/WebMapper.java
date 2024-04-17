package mouse.project.lib.web.mapper;

import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.web.dispatcher.ReqRespContext;
import mouse.project.lib.web.register.RequestMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;

public class WebMapper extends HttpServlet {
    @Serial
    private static final long serialVersionUID = -8925159744550576756L;
    private final Class<?> configClass;

    public WebMapper(Class<?> configClass) {
        this.configClass = configClass;
    }

    private final static Logger logger = LogManager.getLogger(WebMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            processRequest(req, RequestMethod.GET, resp);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            processRequest(req, RequestMethod.POST, resp);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            processRequest(req, RequestMethod.UPDATE, resp);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            processRequest(req, RequestMethod.DELETE, resp);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void processRequest(HttpServletRequest req, RequestMethod method, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        logger.debug("Receive request: " + method + " " + URLTransform.getFullURL(req));
        Ioc.getConfiguredInjector(configClass)
                .get(ReqRespContext.class)
                .useAndExecute(method, req, resp, configClass);
    }
}
