package mouse.project.lib.web.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class StatusException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -590958001767014223L;
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
