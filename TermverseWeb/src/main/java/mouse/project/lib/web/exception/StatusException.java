package mouse.project.lib.web.exception;

import lombok.Getter;

@Getter
public class StatusException extends RuntimeException{
    private final int status;
    public StatusException(int status) {
        super();
        this.status = status;
    }

    public StatusException(int status, String s) {
        super(s);
        this.status = status;
    }
}
