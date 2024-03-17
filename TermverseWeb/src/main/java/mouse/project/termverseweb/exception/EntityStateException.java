package mouse.project.termverseweb.exception;

public class EntityStateException extends RuntimeException {
    public EntityStateException() {
        super();
    }

    public EntityStateException(String message) {
        super(message);
    }

    public EntityStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityStateException(Throwable cause) {
        super(cause);
    }
}
