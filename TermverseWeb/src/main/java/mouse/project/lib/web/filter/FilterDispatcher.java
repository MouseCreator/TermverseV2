package mouse.project.lib.web.filter;

import mouse.project.lib.exception.MultipleImplementationsException;
import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.error.ErrorHandler;
import mouse.project.lib.web.error.ErrorHandlerInvoker;
import mouse.project.lib.web.error.ErrorHandlerMap;
import mouse.project.lib.web.error.ErrorStyle;
import mouse.project.lib.web.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilterDispatcher implements Filter {

    private FilterChainFactory filterChainFactory;

    private ErrorHandlerInvoker errorHandlerInvoker;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configurationClass = filterConfig.getInitParameter("configurationClass");
        try {
            Class<?> clazz = Class.forName(configurationClass);
            Inj inj = Ioc.getConfiguredInjector(clazz);
            filterChainFactory = inj.get(FilterChainFactory.class);
            errorHandlerInvoker = inj.get(ErrorHandlerInvoker.class);
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
        } catch (RuntimeException e) {
            errorHandlerInvoker.processError(e, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void destroy() {

    }
}
