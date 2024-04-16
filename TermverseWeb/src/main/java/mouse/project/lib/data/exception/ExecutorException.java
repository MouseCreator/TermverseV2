package mouse.project.lib.data.exception;

public class ExecutorException extends RuntimeException{
    public ExecutorException(Exception e) {
        super(e);
    }

    public ExecutorException(String message) {
        super(message);
    }
}
