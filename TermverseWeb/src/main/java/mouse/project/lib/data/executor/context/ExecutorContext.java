package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.executor.Executor;

import java.util.function.Function;

public interface ExecutorContext {
    <U> U use(Function<Executor, U> function);
}
