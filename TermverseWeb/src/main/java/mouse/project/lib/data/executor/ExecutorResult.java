package mouse.project.lib.data.executor;

import mouse.project.lib.data.executor.result.AdjustedOptional;
import mouse.project.lib.data.executor.result.AdjustedResult;
import mouse.project.lib.data.executor.result.AdjustedResultList;
import mouse.project.lib.data.transformer.Transformer;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ExecutorResult {
    ResultSet getRaw();
    <T> T to(Transformer<T> transformer);
    <T> T model(Class<T> model);
    <T> List<T> list(Class<T> model);
    <T> Optional<T> optional(Class<T> model);
    <T> AdjustedResult<T> adjusted(Class<T> model);
    <T> AdjustedResultList<T> adjustedList(Class<T> model);
    <T> AdjustedOptional<T> adjustedOptional(Class<T> model);
    void action(Consumer<ExecutorResult> consumer);
}
