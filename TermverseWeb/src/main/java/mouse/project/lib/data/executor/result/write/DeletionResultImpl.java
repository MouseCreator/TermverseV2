package mouse.project.lib.data.executor.result.write;


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
    public int affectedRows() {
        return rowsAffected;
    }
}
