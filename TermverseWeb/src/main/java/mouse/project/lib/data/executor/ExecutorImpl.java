package mouse.project.lib.data.executor;

import mouse.project.lib.data.exception.ExecutorException;
import mouse.project.lib.data.orm.fill.ModelFill;
import mouse.project.lib.data.orm.map.OrmMap;
import mouse.project.lib.ioc.annotation.Auto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecutorImpl implements Executor {
    private final ModelFill fill;
    private final OrmMap map;
    private final Connection connection;

    @Auto
    public ExecutorImpl(ModelFill fill, OrmMap map, Connection connection) {
        this.fill = fill;
        this.map = map;
        this.connection = connection;
    }

    @Override
    public ExecutorResult executeQuery(String sql, Object... args) {
        if (args.length == 0) {
            return executeStatic(sql);
        }
        List<Object> objects = new ArrayList<>(Arrays.asList(args));
        return executeListed(sql, objects);
    }

    @Override
    public ExecutorResult executeListed(String sql, List<Object> argList) {
        if (argList.isEmpty()) {
            return executeStatic(sql);
        }
        return executePrepared(sql, argList);
    }
    private ExecutorResult executeStatic(String sql) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            return toResult(resultSet);
        } catch (Exception e1) {
            throw new ExecutorException(e1);
        }
    }

    private ExecutorResult executePrepared(String sql, List<Object> args) {
        int questionMarks = getQuestionMarks(sql);
        if (args.size() != questionMarks) {
            String msg = String.format("Input sql has %d place holders, but received %d arguments", questionMarks, args.size());
            throw new ExecutorException(msg);
        }
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                setParameter(ps, i, arg);
            }
            ResultSet resultSet = ps.executeQuery();
            return toResult(resultSet);
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }

    private void setParameter(PreparedStatement ps, int i, Object arg) throws SQLException {
        ps.setObject(i, arg);
    }

    private int getQuestionMarks(String sql) {
        long count = sql.chars().filter(ch -> ch == '?').count();
        return (int) count;
    }

    public ExecutorResult toResult(ResultSet resultSet) {
        return new ExecutorResultImpl(resultSet, fill, map);
    }
}
