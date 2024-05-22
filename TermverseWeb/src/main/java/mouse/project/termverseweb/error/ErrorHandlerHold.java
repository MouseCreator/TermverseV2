package mouse.project.termverseweb.error;

import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.error.ErrorHandler;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.response.ErrorResponse;
import mouse.project.termverseweb.config.MouseProjConfig;
import mouse.project.termverseweb.exception.*;

@Service
public class ErrorHandlerHold {
    private static CommonHandler commonHandler() {
        return Ioc.getConfiguredInjector(MouseProjConfig.class).get(CommonHandler.class);
    }
    @Service
    private static class GlobalExceptionHandler implements ErrorHandler<RuntimeException> {
        @Override
        public void handle(RuntimeException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleRuntimeException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<RuntimeException> forType() {
            return RuntimeException.class;
        }
    }
    @Service
    private static class StatusErrorHandler implements ErrorHandler<StatusException> {
        @Override
        public void handle(StatusException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleStatusException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<StatusException> forType() {
            return StatusException.class;
        }
    }
    @Service
    private static class EntityStateHandler implements ErrorHandler<EntityStateException> {
        @Override
        public void handle(EntityStateException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleEntityStateException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<EntityStateException> forType() {
            return EntityStateException.class;
        }
    }

    @Service
    private static class MissingEntityExceptionHandler implements ErrorHandler<MissingEntityException> {
        @Override
        public void handle(MissingEntityException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleMissingEntityException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<MissingEntityException> forType() {
            return MissingEntityException.class;
        }
    }

    @Service
    private static class NoSuchUserExceptionHandler implements ErrorHandler<NoSuchUserException> {
        @Override
        public void handle(NoSuchUserException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleNoSuchUserException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<NoSuchUserException> forType() {
            return NoSuchUserException.class;
        }
    }

    @Service
    private static class RegisterExceptionHandler implements ErrorHandler<RegisterException> {
        @Override
        public void handle(RegisterException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleRegisterException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<RegisterException> forType() {
            return RegisterException.class;
        }
    }

    @Service
    private static class TokenValidationExceptionHandler implements ErrorHandler<TokenValidationException> {
        @Override
        public void handle(TokenValidationException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleTokenValidationException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<TokenValidationException> forType() {
            return TokenValidationException.class;
        }
    }

    @Service
    private static class UnauthorizedExceptionHandler implements ErrorHandler<UnauthorizedException> {
        @Override
        public void handle(UnauthorizedException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleUnauthorizedException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<UnauthorizedException> forType() {
            return UnauthorizedException.class;
        }
    }

    @Service
    private static class UnexpectedAuthTypeExceptionHandler implements ErrorHandler<UnexpectedAuthTypeException> {
        @Override
        public void handle(UnexpectedAuthTypeException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleUnexpectedAuthTypeException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<UnexpectedAuthTypeException> forType() {
            return UnexpectedAuthTypeException.class;
        }
    }

    @Service
    private static class UserAlreadyExistsExceptionHandler implements ErrorHandler<UserAlreadyExistsException> {
        @Override
        public void handle(UserAlreadyExistsException ex, ErrorResponse response) {
            ErrorResponse errorResponse = commonHandler().handleUserAlreadyExistsException(ex);
            response.from(errorResponse);
        }
        @Override
        public Class<UserAlreadyExistsException> forType() {
            return UserAlreadyExistsException.class;
        }
    }



}
