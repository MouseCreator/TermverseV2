package mouse.project.lib.data.pool;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionDecorated implements PooledConnection {

    private final Connection decorated;
    public final PutConnectionPool source;
    private boolean closed = false;
    private long lastGet;
    private boolean isAdditional = false;
    public ConnectionDecorated(Connection decorated, PutConnectionPool source) {
        this.decorated = decorated;
        this.source = source;
        lastGet = System.currentTimeMillis();
    }
    public ConnectionDecorated(Connection decorated, PutConnectionPool source, boolean isAdditional) {
        this.decorated = decorated;
        this.source = source;
        this.isAdditional = true;
        lastGet = System.currentTimeMillis();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return decorated.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return decorated.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return decorated.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return decorated.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        decorated.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return decorated.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        decorated.commit();
    }

    @Override
    public void rollback() throws SQLException {
        decorated.rollback();
    }

    @Override
    public void close() throws SQLException {
        closed = true;
        source.put(this);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return decorated.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        decorated.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return decorated.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        decorated.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return decorated.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        decorated.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return decorated.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return decorated.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        decorated.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return decorated.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return decorated.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return decorated.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return decorated.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        decorated.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        decorated.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return decorated.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return decorated.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return decorated.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        decorated.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        decorated.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return decorated.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return decorated.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return decorated.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return decorated.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return decorated.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return decorated.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return decorated.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return decorated.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return decorated.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return decorated.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return decorated.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        decorated.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        decorated.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return decorated.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return decorated.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return decorated.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return decorated.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        decorated.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return decorated.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        decorated.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        decorated.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return decorated.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return decorated.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return decorated.isWrapperFor(iface);
    }

    @Override
    public void onGet() {
        lastGet = System.currentTimeMillis();
        closed = false;
    }

    @Override
    public void onReturn() {

    }

    @Override
    public long lastGivenTime() {
        return lastGet;
    }

    @Override
    public boolean isTimeout(int timeout) {
        return System.currentTimeMillis() - lastGet > timeout && isAdditional;
    }

    public boolean isAdditional() {
        return isAdditional;
    }
}
