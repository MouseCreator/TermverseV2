package mouse.project.lib.web.exception;

public class NotFoundException extends StatusException{
    public NotFoundException() {
        super(404);
    }

    public NotFoundException(String s) {
        super(404, s);
    }
}
