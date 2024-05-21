package mouse.project.lib.web.exception;

import java.io.Serial;

public class NotFoundException extends StatusException{
    @Serial
    private static final long serialVersionUID = 5304935729443949771L;

    public NotFoundException() {
        super(404);
    }

    public NotFoundException(String s) {
        super(404, s);
    }
}
