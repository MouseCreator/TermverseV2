package mouse.project.lib.web.exception;

import java.io.IOException;

public class RequestProcessException extends RuntimeException{
    public RequestProcessException(IOException e) {
        super(e);
    }
}
