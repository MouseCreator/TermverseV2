package mouse.project.lib.exception;

public class MissingAnnotationException extends RuntimeException{
    public MissingAnnotationException() {
    }

    public MissingAnnotationException(String message) {
        super(message);
    }

    public MissingAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAnnotationException(Throwable cause) {
        super(cause);
    }
}
