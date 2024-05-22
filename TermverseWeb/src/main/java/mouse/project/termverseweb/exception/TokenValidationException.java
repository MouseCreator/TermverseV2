package mouse.project.termverseweb.exception;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String msg, Exception e) {
        super(msg, e);
    }
}
