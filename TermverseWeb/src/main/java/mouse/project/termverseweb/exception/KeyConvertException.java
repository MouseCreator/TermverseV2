package mouse.project.termverseweb.exception;

public class KeyConvertException extends RuntimeException{
    public KeyConvertException(String s) {
        super(s);
    }

    public KeyConvertException(String s, Exception e) {
        super(s, e);
    }
}
