package mouse.project.lib.data.executor.result.read;


import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface AdjustedOptional<T> {
    AdjustedOptional<T> apply(Consumer<T> consumer);
    Optional<T> get();
    <U> AdjustedOptional<U> map(Function<T, U> mapper);
}
