package mouse.project.lib.data.executor.result.read;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdjustedOptionalImpl<T> implements AdjustedOptional<T> {

    private final T model;

    public AdjustedOptionalImpl(T model) {
        this.model = model;
    }

    @Override
    public AdjustedOptional<T> apply(Consumer<T> consumer) {
        if (model == null) {
            return this;
        }
        consumer.accept(model);
        return this;
    }

    @Override
    public Optional<T> get() {
        return Optional.ofNullable(model);
    }

    @Override
    public <U> AdjustedOptional<U> map(Function<T, U> mapper) {
        if (model == null) {
            return new AdjustedOptionalImpl<>(null);
        }
        return new AdjustedOptionalImpl<>(mapper.apply(model));
    }
}
