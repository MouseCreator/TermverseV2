package mouse.project.lib.data.executor;

import mouse.project.lib.data.executor.context.ReadExecutor;
import mouse.project.lib.data.executor.context.WriteExecutor;

import java.util.function.Function;

public interface Executor {
    <U> U write(Function<WriteExecutor, U> function);
    <U> U read(Function<ReadExecutor, U> function);
}
