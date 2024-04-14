package mouse.project.lib.data.executor.context;

import mouse.project.lib.data.exception.ExecutorException;
import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.data.executor.ExecutorImpl;
import mouse.project.lib.data.orm.fill.ModelFill;
import mouse.project.lib.data.orm.map.OrmMap;
import mouse.project.lib.data.pool.ConnectionPool;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Service
public class ExecutorContextImpl implements ExecutorContext {
    private final ConnectionPool pool;
    private final ModelFill fill;
    private final OrmMap map;
    @Auto
    public ExecutorContextImpl(ConnectionPool pool, ModelFill fill, OrmMap map) {
        this.pool = pool;
        this.fill = fill;
        this.map = map;
    }

    @Override
    public <U> U write(Function<Executor, U> function) {
        List<Statement> toClose = new ArrayList<>();
        try(Connection connection = pool.getConnection()) {
            try {
                Executor executor = new ExecutorImpl(fill, map, connection, toClose);
                U result = function.apply(executor);
                connection.commit();
                closeAll(toClose);
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw new ExecutorException(e);
            }
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }

    private void closeAll(List<Statement> toClose) throws SQLException {
        for (Statement s : toClose) {
            s.close();
        }
    }

    @Override
    public <U> U read(Function<Executor, U> function) {
        List<Statement> toClose = new ArrayList<>();
        try(Connection connection = pool.getConnection()) {
            try {
                Executor executor = new ExecutorImpl(fill, map, connection, toClose);
                U result = function.apply(executor);
                closeAll(toClose);
                return result;
            } catch (Exception e) {
                throw new ExecutorException(e);
            }
        } catch (SQLException e) {
            throw new ExecutorException(e);
        }
    }
}
