package mouse.project.lib.exception;

public class CycleDependencyException extends RuntimeException{
    public CycleDependencyException(String message) {
        super(message);
    }
}
