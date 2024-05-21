package mouse.project.lib.web.error;

import mouse.project.lib.exception.MultipleImplementationsException;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.response.ErrorResponse;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ErrorHandlerInvoker {
    private final ErrorHandlerMap errorHandlerMap;
    private ErrorStyle errorStyle;
    @Auto
    public ErrorHandlerInvoker(ErrorHandlerMap errorHandlerMap) {
        this.errorHandlerMap = errorHandlerMap;
    }
    @After
    public void withErrorStyle(List<ErrorStyle> errorStyles) {
        if (errorStyles.isEmpty()) {
            errorStyle = ErrorResponse::getMessage;
        }
        if (errorStyles.size() > 2) {
            throw new MultipleImplementationsException("Multiple implementations found for: " + ErrorStyle.class);
        }
        errorStyle = errorStyles.getFirst();
    }

    public void processError(RuntimeException e, ServletResponse servletResponse) {
        Optional<ErrorHandler<?>> errorHandlerOpt = errorHandlerMap.onException(e);
        ErrorResponse errorResponse = ErrorResponse.def();
        try {
            if (errorHandlerOpt.isPresent()) {
                ErrorHandler<?> errorHandler = errorHandlerOpt.get();
                errorHandler.handleDefault(e, errorResponse);
            }
        } catch (Exception ex) {
            errorResponse.setStatus(500);
            errorResponse.setMessage("Filter error");
        }
        writeToResponse(errorResponse, servletResponse);
    }

    private void writeToResponse(ErrorResponse errorResponse, ServletResponse servletResponse) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            response.setStatus(errorResponse.getStatus());
            String errorBody = errorStyle.styleError(errorResponse);
            response.getWriter().write(errorBody);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void onFatalError(RuntimeException e, HttpServletResponse response) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setMessage("Fatal:" + e.getMessage());
        writeToResponse(errorResponse, response);
    }
}
