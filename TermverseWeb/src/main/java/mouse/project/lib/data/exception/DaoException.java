package mouse.project.lib.data.exception;


import java.io.Serial;

public class DaoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 8258805303642237800L;

    public DaoException(Exception e) {
        super(e);
    }

    public DaoException(String s, Exception ex) {
        super(s, ex);
    }
}
