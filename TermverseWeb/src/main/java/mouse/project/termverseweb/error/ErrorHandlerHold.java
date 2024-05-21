package mouse.project.termverseweb.error;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.error.ErrorHandler;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.response.ErrorResponse;

@Service
public class ErrorHandlerHold {
    @Service
    private static class StatusErrorHandler implements ErrorHandler<StatusException> {
        @Override
        public void handle(StatusException exception, ErrorResponse response) {
            int status = exception.getStatus();
            String message = exception.getMessage();
            response.setMessage(message);
            response.setStatus(status);
        }

        @Override
        public Class<StatusException> forType() {
            return StatusException.class;
        }
    }

    @Service
    private static class GlobalExceptionHandler implements ErrorHandler<RuntimeException> {
        @Override
        public void handle(RuntimeException exception, ErrorResponse response) {
            String message = exception.getMessage();
            if (message == null || message.isEmpty()) {
                message = "Server error";
            }
            response.setMessage(message);
            response.setStatus(500);
        }

        @Override
        public Class<RuntimeException> forType() {
            return RuntimeException.class;
        }
    }
}
