package mouse.project.lib.files.exception;

import java.io.Serial;

public class PropertyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3240930277797464236L;

    public PropertyException(String s) {
        super(s);
    }
}
