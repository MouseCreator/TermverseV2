package mouse.project.lib.data.executor.result.write;

import mouse.project.lib.data.exception.ExecutorException;

import java.sql.ResultSet;
import java.util.function.Consumer;

public interface WriteResult {
    <U> WriteResult singleKey(Class<U> keyClass, Consumer<U> keyConsumer);
    WriteResult onGeneratedKeys(Consumer<ResultSet> keys);
    default WriteResult assertAffectedAtLeast(Integer integer) {
        if (affectedRows() < integer) {
            throw new ExecutorException("Affected " + affectOne() + " rows, but expected: " + integer);
        }
        return this;
    }
    default WriteResult affectOne() {
        if (affectedRows() != 1) {
            throw new ExecutorException("Affected " + affectedRows() + " rows, but expected: 1");
        }
        return this;
    }
    int affectedRows();
}
