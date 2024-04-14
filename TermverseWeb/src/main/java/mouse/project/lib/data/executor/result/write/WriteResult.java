package mouse.project.lib.data.executor.result.write;

import java.sql.ResultSet;
import java.util.function.Consumer;

public interface WriteResult {
    <U> WriteResult singleKey(Class<U> keyClass, Consumer<U> keyConsumer);
    WriteResult onGeneratedKeys(Consumer<ResultSet> keys);
    WriteResult assertAffectedAtLeast(Integer integer);
}
