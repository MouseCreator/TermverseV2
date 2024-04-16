package mouse.project.lib.files.exception;

import java.io.Serial;

public class FileException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -2682428058099684407L;

    public FileException(Throwable cause) {
        super(cause);
    }
}
