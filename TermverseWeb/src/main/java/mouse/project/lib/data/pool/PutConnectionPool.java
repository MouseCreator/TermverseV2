package mouse.project.lib.data.pool;

public interface PutConnectionPool extends ConnectionPool {
    void put(PooledConnection connection);
}
