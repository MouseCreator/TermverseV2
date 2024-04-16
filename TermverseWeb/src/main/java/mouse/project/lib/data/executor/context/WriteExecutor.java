package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.executor.result.write.WriteResult;

import java.util.List;

public interface WriteExecutor {
    WriteResult execute(String sql, Object... args);
    WriteResult executeListed(String sql, List<?> args);

}
