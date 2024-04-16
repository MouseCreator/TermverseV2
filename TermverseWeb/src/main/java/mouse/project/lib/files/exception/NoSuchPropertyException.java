package mouse.project.lib.files.exception;

import java.io.Serial;

public class NoSuchPropertyException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 8908071497307623087L;

    public NoSuchPropertyException(String message) {
        super(message);
    }
}
