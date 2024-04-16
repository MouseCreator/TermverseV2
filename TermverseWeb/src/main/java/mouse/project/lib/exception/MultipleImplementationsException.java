package mouse.project.lib.exception;

public class MultipleImplementationsException extends RuntimeException{
    public MultipleImplementationsException() {
    }

    public MultipleImplementationsException(String message) {
        super(message);
    }

    public MultipleImplementationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleImplementationsException(Throwable cause) {
        super(cause);
    }
}
