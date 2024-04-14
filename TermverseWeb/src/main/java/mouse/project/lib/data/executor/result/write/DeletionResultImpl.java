package mouse.project.lib.data.executor.result.write;


import mouse.project.lib.data.exception.ExecutorException;

import java.sql.ResultSet;
import java.util.function.Consumer;

public class DeletionResultImpl implements WriteResult {
    private final int rowsAffected;

    public DeletionResultImpl(int affected) {
        this.rowsAffected = affected;
    }

    @Override
    public <U> WriteResult singleKey(Class<U> keyClass, Consumer<U> keyConsumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteResult onGeneratedKeys(Consumer<ResultSet> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteResult assertAffectedAtLeast(Integer integer) {
        if (rowsAffected < integer) {
            throw new ExecutorException("Affected " + rowsAffected + " rows, but expected: " + integer);
        }
        return this;
    }

    @Override
    public WriteResult affectOne() {
        if (rowsAffected != 1) {
            throw new ExecutorException("Affected " + rowsAffected + " rows, but expected: 1");
        }
        return this;
    }
}
