package mouse.project.lib.data.executor.result;

import mouse.project.lib.data.exception.DaoException;

import java.sql.ResultSet;

public class Raw {
    public static int getInt(ResultSet r) {
        try {
            r.next();
            return r.getInt(1);
        } catch (Exception ex) {
            throw new DaoException("Unable to get integer from result set", ex);
        }
    }

    public static boolean getBoolean(ResultSet r) {
        try {
            r.next();
            return r.getBoolean(1);
        } catch (Exception ex) {
            throw new DaoException("Unable to get boolean from result set", ex);
        }
    }
}
