package mouse.project.lib.exception;

public class ImplementationAccessException extends RuntimeException {
    public ImplementationAccessException() {
    }

    public ImplementationAccessException(String message) {
        super(message);
    }

    public ImplementationAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementationAccessException(Throwable cause) {
        super(cause);
    }
}
