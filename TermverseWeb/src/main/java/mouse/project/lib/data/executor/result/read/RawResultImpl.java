package mouse.project.lib.data.executor.result.read;

import mouse.project.lib.data.exception.ExecutorException;

import java.sql.ResultSet;
import java.util.function.Consumer;
import java.util.function.Function;

public class RawResultImpl implements RawResult {
    private final ResultSet resultSet;
    public RawResultImpl(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public <U> U map(Function<ResultSet, U> mapper) {
        try {
            return mapper.apply(resultSet);
        } catch (Exception e) {
            throw new ExecutorException(e);
        }
    }

    @Override
    public RawResult action(Consumer<ResultSet> consumer) {
        consumer.accept(resultSet);
        return this;
    }

    @Override
    public void map(Consumer<ResultSet> consumer) {
        consumer.accept(resultSet);
    }
}
