package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.executor.Executor;

import java.util.function.Function;

public interface ExecutorContext {
    <U> U write(Function<Executor, U> function);
    <U> U read(Function<Executor, U> function);
}
