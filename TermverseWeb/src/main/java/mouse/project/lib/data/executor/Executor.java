package mouse.project.lib.data.executor;

import java.util.List;

public interface Executor {
    ExecutorResult executeQuery(String sql, Object... args);
    ExecutorResult executeQuery(String sql, List<Object> argList);
}
