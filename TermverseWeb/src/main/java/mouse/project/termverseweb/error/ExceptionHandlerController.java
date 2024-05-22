package mouse.project.termverseweb.error;

import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.response.ErrorResponse;
import mouse.project.termverseweb.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private final CommonHandler commonHandler;
    @Autowired
    public ExceptionHandlerController(CommonHandler commonHandler) {
        this.commonHandler = commonHandler;
    }
    private ResponseEntity<ErrorResponse> toResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(RuntimeException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleRuntimeException(ex));
    }
    @ExceptionHandler(StatusException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(StatusException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleStatusException(ex));
    }

    @ExceptionHandler(EntityStateException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(EntityStateException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleEntityStateException(ex));
    }

    @ExceptionHandler(MissingEntityException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(MissingEntityException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleMissingEntityException(ex));
    }
    @ExceptionHandler(NoSuchUserException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(NoSuchUserException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleNoSuchUserException(ex));
    }
    @ExceptionHandler(RegisterException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(RegisterException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleRegisterException(ex));
    }
    @ExceptionHandler(TokenValidationException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(TokenValidationException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleTokenValidationException(ex));
    }
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(UnauthorizedException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleUnauthorizedException(ex));
    }
    @ExceptionHandler(UnexpectedAuthTypeException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(UnexpectedAuthTypeException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleUnexpectedAuthTypeException(ex));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(UserAlreadyExistsException ex, WebRequest request) {
        return toResponseEntity(commonHandler.handleUserAlreadyExistsException(ex));
    }
}
