package mouse.project.lib.web.filter;

import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Service
public class FilterDispatcher implements Filter {

    private FilterChainFactory filterChainFactory;

    private final Logger logger = LogManager.getLogger(FilterDispatcher.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configurationClass = filterConfig.getInitParameter("configurationClass");
        try {
            Class<?> clazz = Class.forName(configurationClass);
            filterChainFactory = Ioc.getConfiguredInjector(clazz).get(FilterChainFactory.class);
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        mouse.project.lib.web.filter.FilterChain chain = filterChainFactory.createChain(servletRequest, servletResponse);
        try {
            while (chain.hasNext()) {
                chain.invokeNext();
            }
        } catch (StatusException e) {
            sendError(e, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void sendError(StatusException e, ServletResponse servletResponse) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            logger.debug(e.getMessage());
            response.sendError(e.getStatus());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void destroy() {

    }
}
