package mouse.project.lib.data.pool;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();
}
