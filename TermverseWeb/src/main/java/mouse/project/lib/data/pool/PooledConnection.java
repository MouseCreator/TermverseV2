package mouse.project.lib.data.pool;

import java.sql.Connection;

public interface PooledConnection extends Connection {
    void onGet();
    void onReturn();
    long lastGivenTime();
    boolean isTimeout(int timeout);

}
