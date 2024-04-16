package mouse.project.lib.data.executor.result.read;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface AdjustedResultList<T> {
    AdjustedResultList<T> apply(Consumer<T> consumer);
    List<T> get();
    <U> AdjustedResultList<U> map(Function<T, U> mapper);

}
