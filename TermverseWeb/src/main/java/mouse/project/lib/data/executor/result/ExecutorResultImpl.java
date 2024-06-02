package mouse.project.lib.data.executor.result;

import mouse.project.lib.data.executor.result.read.*;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.data.orm.fill.ModelFill;
import mouse.project.lib.data.orm.map.OrmMap;
import mouse.project.lib.data.transformer.Transformer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ExecutorResultImpl implements ReadResult {

    private final ResultSet resultSet;
    private final ModelFill fill;
    private final OrmMap map;

    public ExecutorResultImpl(ResultSet resultSet, ModelFill fill, OrmMap map) {
        this.fill = fill;
        this.resultSet = resultSet;
        this.map = map;
    }

    @Override
    public RawResult getRaw() {
        return new RawResultImpl(resultSet);
    }

    @Override
    public <T> T to(Transformer<T> transformer) {
        return transformer.apply(resultSet);
    }

    @Override
    public <T> T model(Class<T> model) {
        ModelDescription<?> modelDescription = map.get(model);
        Object object = fill.createAndFill(resultSet, modelDescription);
        return model.cast(object);
    }

    @Override
    public <T> List<T> list(Class<T> model) {
        ModelDescription<?> modelDescription = map.get(model);
        List<?> list = fill.createList(resultSet, modelDescription);
        List<T> result = new ArrayList<>();
        for (Object obj : list) {
            result.add(model.cast(obj));
        }
        return result;
    }

    @Override
    public <T> Optional<T> optional(Class<T> model) {
        ModelDescription<?> modelDescription = map.get(model);
        Optional<?> optional = fill.createAndOptional(resultSet, modelDescription);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        return optional.map(model::cast);
    }

    @Override
    public <T> AdjustedResult<T> adjusted(Class<T> model) {
        return new AdjustedResultImpl<>(model(model));
    }

    @Override
    public <T> AdjustedResultList<T> adjustedList(Class<T> model) {
        List<T> list = list(model);
        return new AdjustedResultListImpl<>(list);
    }

    @Override
    public <T> AdjustedOptional<T> adjustedOptional(Class<T> model) {
        return new AdjustedOptionalImpl<>(optional(model).orElse(null));
    }

    @Override
    public void action(Consumer<ReadResult> consumer) {
        consumer.accept(this);
    }
}
