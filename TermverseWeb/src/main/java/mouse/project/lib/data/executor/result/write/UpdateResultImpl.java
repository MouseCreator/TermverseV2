package mouse.project.lib.data.executor.result.write;

import java.sql.ResultSet;
import java.util.function.Consumer;

public class UpdateResultImpl implements WriteResult {
    private final int rowsAffected;

    public UpdateResultImpl(int affected) {
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
