package mouse.project.termverseweb.exception;

import java.io.Serial;

public class FilterException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -8700081737423711620L;

    public FilterException(String s) {
        super(s);
    }

    public FilterException(Exception e) {
        super(e);
    }
}
