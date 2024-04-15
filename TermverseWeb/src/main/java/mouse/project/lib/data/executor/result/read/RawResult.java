package mouse.project.lib.data.executor.result.read;

import java.sql.ResultSet;
import java.util.function.Consumer;
import java.util.function.Function;

public interface RawResult {
    <U> U map(Function<ResultSet, U> mapper);
    RawResult action(Consumer<ResultSet> consumer);
    void map(Consumer<ResultSet> consumer);
}
