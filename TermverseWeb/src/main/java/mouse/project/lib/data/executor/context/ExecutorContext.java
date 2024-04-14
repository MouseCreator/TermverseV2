package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.data.executor.WriteExecutor;

import java.util.function.Function;

public interface ExecutorContext {
    <U> U write(Function<WriteExecutor, U> function);
    <U> U read(Function<Executor, U> function);
}
