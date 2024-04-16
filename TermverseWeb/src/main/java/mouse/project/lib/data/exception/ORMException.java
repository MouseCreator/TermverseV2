package mouse.project.lib.data.exception;

import java.sql.SQLException;

public class ORMException extends RuntimeException {

    public ORMException(String s, Exception e) {
        super(s, e);
    }

    public ORMException(String s) {
        super(s);
    }

    public ORMException(SQLException e) {
        super(e);
    }
}
