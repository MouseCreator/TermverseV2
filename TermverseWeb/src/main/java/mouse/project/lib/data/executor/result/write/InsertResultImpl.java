package mouse.project.lib.data.executor.result.write;

import mouse.project.lib.data.exception.ExecutorException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class InsertResultImpl implements WriteResult {
    private final PreparedStatement statement;
    private final int rowsAffected;

    public InsertResultImpl(PreparedStatement statement, int rowsAffected) {
        this.statement = statement;
        this.rowsAffected = rowsAffected;
    }

    @Override
    public <U> WriteResult singleKey(Class<U> keyClass, Consumer<U> keyConsumer) {
        try {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            U object = generatedKeys.getObject(1, keyClass);
            keyConsumer.accept(object);
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
        return this;
    }

    @Override
    public WriteResult onGeneratedKeys(Consumer<ResultSet> keys) {
        try {
            keys.accept(statement.getGeneratedKeys());
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
        return this;
    }

    @Override
    public int affectedRows() {
        return rowsAffected;
    }


}
