package mouse.project.termverseweb.exception;

public class MissingEntityException extends RuntimeException{
    public MissingEntityException() {
    }

    public MissingEntityException(String message) {
        super(message);
    }

    public MissingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingEntityException(Throwable cause) {
        super(cause);
    }
}
