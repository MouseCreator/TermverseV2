package mouse.project.lib.data.executor.result.read;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdjustedResultListImpl<T> implements AdjustedResultList<T> {

    private final List<T> models;

    public AdjustedResultListImpl(List<T> models) {
        this.models = models;
    }

    @Override
    public AdjustedResultList<T> apply(Consumer<T> consumer) {
        models.forEach(consumer);
        return this;
    }

    @Override
    public List<T> get() {
        return new ArrayList<>(models);
    }

    @Override
    public <U> AdjustedResultList<U> map(Function<T, U> mapper) {
        List<U> list = models.stream().map(mapper).toList();
        return new AdjustedResultListImpl<>(list);
    }
}
