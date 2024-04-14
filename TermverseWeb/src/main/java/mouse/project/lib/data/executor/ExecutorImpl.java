package mouse.project.lib.data.executor;

import mouse.project.lib.data.exception.ExecutorException;
import mouse.project.lib.data.executor.result.read.ReadResult;
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
    private final List<Statement> toClose;
    @Auto
    public ExecutorImpl(ModelFill fill, OrmMap map, Connection connection, List<Statement> toClose) {
        this.fill = fill;
        this.map = map;
        this.connection = connection;
        this.toClose = toClose;
    }

    @Override
    public ReadResult executeQuery(String sql, Object... args) {
        if (args.length == 0) {
            return executeStatic(sql);
        }
        List<Object> objects = new ArrayList<>(Arrays.asList(args));
        return executePrepared(sql, objects);
    }

    @Override
    public ReadResult executeListed(String sql, List<Object> argList) {
        if (argList.isEmpty()) {
            return executeStatic(sql);
        }
        return executePrepared(sql, argList);
    }
    private ReadResult executeStatic(String sql) {
        try {
            Statement statement = connection.createStatement();
            toClose.add(statement);
            ResultSet resultSet = statement.executeQuery(sql);
            return toResult(resultSet);
        } catch (Exception e1) {
            throw new ExecutorException(e1);
        }
    }

    private ReadResult executePrepared(String sql, List<Object> args) {
        QMarks.validateQMarks(sql, args);
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            toClose.add(ps);
            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                setParameter(ps, i+1, arg);
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



    public ReadResult toResult(ResultSet resultSet) {
        return new ExecutorResultImpl(resultSet, fill, map);
    }
}
