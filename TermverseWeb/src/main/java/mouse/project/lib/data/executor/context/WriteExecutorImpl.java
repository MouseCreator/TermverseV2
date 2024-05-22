package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.exception.ExecutorException;
import mouse.project.lib.data.executor.QMarks;
import mouse.project.lib.data.executor.result.write.*;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteExecutorImpl implements WriteExecutor {

    private final Connection connection;
    private final List<Statement> toClose;

    public WriteExecutorImpl(Connection connection, List<Statement> toClose) {
        this.connection = connection;
        this.toClose = toClose;
    }

    @Override
    public WriteResult execute(String sql, Object... args) {
        return executeListed(sql, fromArgs(args));
    }

    public WriteResult executeListed(String sql, List<?> args) {
        String[] split = sql.split("\\s", 2);
        String method = split[0];
        String upperCase = method.trim().toUpperCase();
        List<Object> arObjects = new ArrayList<>(args);
        return switch (upperCase) {
            case "INSERT" -> onInsertListed(sql, arObjects);
            case "DELETE" -> onDeleteListed(sql, arObjects);
            case "UPDATE" -> onUpdateListed(sql, arObjects);
            default -> throw new ExecutorException("Unknown method: " + upperCase);
        };
    }
    @NotNull
    private static ArrayList<Object> fromArgs(Object[] args) {
        return new ArrayList<>(Arrays.asList(args));
    }

    private AfterExecute executeWrittenList(String sql, List<Object> args) {
        QMarks.validateQMarks(sql, args);
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            prepare(args, ps);
            int rowsAffected = ps.executeUpdate();
            return new AfterExecute(ps, rowsAffected);
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }

    private record AfterExecute(PreparedStatement statement, int rows) {
    }

    private void setParameter(PreparedStatement ps, int i, Object arg) throws SQLException {
        ps.setObject(i, arg);
    }
    public WriteResult onUpdateListed(String sql, List<Object> args) {
        AfterExecute afterExecute = executeWrittenList(sql, args);
        return new UpdateResultImpl(afterExecute.rows());
    }
    private AfterExecute onInsertionWrite(String sql, Object... args) {
        List<Object> objects = fromArgs(args);
        return onInsertionWriteListed(sql, objects);
    }
    private AfterExecute onInsertionWriteListed(String sql, List<Object> args) {
        QMarks.validateQMarks(sql, args);
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepare(args, ps);
            int rowsAffected = ps.executeUpdate();
            return new AfterExecute(ps, rowsAffected);
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }

    private void prepare(List<Object> args, PreparedStatement ps) throws SQLException {
        toClose.add(ps);
        for (int i = 0; i < args.size(); i++) {
            Object arg = args.get(i);
            setParameter(ps, i+1, arg);
        }
    }

    public WriteResult onInsertListed(String sql, List<Object> args) {
        AfterExecute insertResult = onInsertionWriteListed(sql, args);
        return new InsertResultImpl(insertResult.statement(), insertResult.rows());
    }


    public WriteResult onDeleteListed(String sql, List<Object> args) {
        AfterExecute afterExecute = executeWrittenList(sql, args);
        int rows = afterExecute.rows();
        return new DeletionResultImpl(rows);
    }
}
