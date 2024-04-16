package mouse.project.lib.exception;

public class ScanException extends RuntimeException{

    public ScanException() {
    }

    public ScanException(String message) {
        super(message);
    }

    public ScanException(Throwable cause) {
        super(cause);
    }
}
