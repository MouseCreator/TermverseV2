package mouse.project.lib.web.exception;

public class BodyParsingException extends RuntimeException{
    public BodyParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BodyParsingException(String message) {
        super(message);
    }
}
