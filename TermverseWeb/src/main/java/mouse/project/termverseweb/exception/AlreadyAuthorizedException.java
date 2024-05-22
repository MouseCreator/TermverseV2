package mouse.project.termverseweb.exception;

public class AlreadyAuthorizedException extends RuntimeException{
    public AlreadyAuthorizedException() {
    }

    public AlreadyAuthorizedException(String message) {
        super(message);
    }
}
