package mouse.project.lib.data.pool;

import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.data.provider.ConnectionProvider;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CyclePutConnectionPool implements PutConnectionPool {
    private final ConnectionProvider provider;
    private final int HOLD_CONNECTIONS;
    private final int TIMEOUT;
    protected final List<PooledConnection> list;
    private final Object obj;
    @Auto
    public CyclePutConnectionPool(ConnectionProvider provider) {
        this.provider = provider;
        TIMEOUT = 5000;
        HOLD_CONNECTIONS = 5;
        obj = new Object();
        list = new ArrayList<>(HOLD_CONNECTIONS);
    }

    public CyclePutConnectionPool(ConnectionProvider provider, int hold, int timeout) {
        this.provider = provider;
        this.TIMEOUT = timeout;
        this.HOLD_CONNECTIONS = hold;
        obj = new Object();
        list = new ArrayList<>(HOLD_CONNECTIONS);
    }

    @After
    public void fill() {
        for (int i = 0; i < HOLD_CONNECTIONS; i++) {
            PooledConnection connection = createPooledConnection(false);
            list.add(connection);
        }
    }

    private PooledConnection createPooledConnection(boolean additional) {
        Connection toDecorate = provider.provide();
        try {
            toDecorate.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ORMException(e);
        }
        return new ConnectionDecorated(toDecorate, this, additional);
    }

    @Override
    public Connection getConnection() {
        synchronized (obj) {
            PooledConnection connection;
            if (list.isEmpty()) {
                connection = createPooledConnection(true);
            } else {
                connection = list.remove(0);
                while (connection.isTimeout(TIMEOUT) && !list.isEmpty()) {
                    connection = list.remove(0);
                }
            }
            connection.onGet();
            return connection;
        }
    }

    @Override
    public void put(PooledConnection connection) {
        synchronized (obj) {
            list.add(connection);
            connection.onReturn();
        }
    }
}
