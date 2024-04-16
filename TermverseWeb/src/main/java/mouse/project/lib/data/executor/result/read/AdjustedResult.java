package mouse.project.lib.data.executor.result.read;

import java.util.function.Consumer;
import java.util.function.Function;

public interface AdjustedResult<T> {
    T get();
    AdjustedResult<T> apply(Consumer<T> action);
    <U> AdjustedResult<U> map(Function<T, U> mapper);
}
