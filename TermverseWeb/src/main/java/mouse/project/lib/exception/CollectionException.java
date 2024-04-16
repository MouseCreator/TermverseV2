package mouse.project.lib.exception;

public class CollectionException extends RuntimeException{
    public CollectionException() {
    }

    public CollectionException(String message) {
        super(message);
    }

    public CollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionException(Throwable cause) {
        super(cause);
    }
}
