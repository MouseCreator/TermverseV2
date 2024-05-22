package mouse.project.termverseweb.error;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.response.ErrorResponse;
import mouse.project.termverseweb.exception.*;

@Service
@org.springframework.stereotype.Service
public class CommonHandler {
    private ErrorResponse response(int status, String message) {
        return new ErrorResponse(status, message, "");
    }
    private ErrorResponse response(int status, String message, String details) {
        return new ErrorResponse(status, message, details);
    }
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return response(500, "Internal server error", ex.getMessage());
    }
    public ErrorResponse handleStatusException(StatusException ex) {
        return response(ex.getStatus(), "Internal server error", ex.getMessage());
    }

    public ErrorResponse handleEntityStateException(EntityStateException ex) {
        return response(500, "Illegal entity state", ex.getMessage());
    }

    public ErrorResponse handleMissingEntityException(MissingEntityException ex) {
        return response(404, "Entity not found", ex.getMessage());
    }
    public ErrorResponse handleNoSuchUserException(NoSuchUserException ex) {
        return response(404, "Entity not found", ex.getMessage());
    }
    public ErrorResponse handleRegisterException(RegisterException ex) {
        return response(500, "Failed to register", ex.getMessage());
    }
    public ErrorResponse handleTokenValidationException(TokenValidationException ex) {
        return response(403, "Invalid token", ex.getMessage());
    }
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        return response(401, "Unauthorized", ex.getMessage());
    }
    public ErrorResponse handleUnexpectedAuthTypeException(UnexpectedAuthTypeException ex) {
        return response(500, "Unexpected authorization type", ex.getMessage());
    }

    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return response(400, "User already exists", ex.getMessage());
    }
}
