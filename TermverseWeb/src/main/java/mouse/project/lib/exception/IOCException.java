package mouse.project.lib.exception;

public class IOCException extends RuntimeException{
    public IOCException() {
    }

    public IOCException(String message) {
        super(message);
    }

    public IOCException(String message, Throwable cause) {
        super(message, cause);
    }

    public IOCException(Throwable cause) {
        super(cause);
    }

    public IOCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
