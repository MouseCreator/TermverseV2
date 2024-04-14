package mouse.project.lib.data.executor.result.read;

import java.util.function.Consumer;
import java.util.function.Function;

public class AdjustedResultImpl<T> implements AdjustedResult<T> {

    private final T model;

    public AdjustedResultImpl(T model) {
        this.model = model;
    }

    @Override
    public T get() {
        return model;
    }

    @Override
    public AdjustedResult<T> apply(Consumer<T> action) {
        action.accept(model);
        return this;
    }

    @Override
    public <U> AdjustedResult<U> map(Function<T, U> mapper) {
        return new AdjustedResultImpl<>(mapper.apply(model));
    }
}
