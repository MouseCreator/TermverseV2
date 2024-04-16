package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.executor.result.read.ReadResult;

import java.util.List;

public interface ReadExecutor {
    ReadResult executeQuery(String sql, Object... args);
    ReadResult executeListed(String sql, List<?> argList);
}
