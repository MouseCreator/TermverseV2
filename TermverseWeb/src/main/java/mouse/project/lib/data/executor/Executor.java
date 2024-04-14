package mouse.project.lib.data.executor;

import mouse.project.lib.data.executor.result.read.ReadResult;

import java.util.List;

public interface Executor {
    ReadResult executeQuery(String sql, Object... args);
    ReadResult executeListed(String sql, List<Object> argList);
}
