package mouse.project.lib.exception;

public class NoCardDefinitionException extends RuntimeException {
    public NoCardDefinitionException() {
    }

    public NoCardDefinitionException(String message) {
        super(message);
    }

    public NoCardDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCardDefinitionException(Throwable cause) {
        super(cause);
    }
}
