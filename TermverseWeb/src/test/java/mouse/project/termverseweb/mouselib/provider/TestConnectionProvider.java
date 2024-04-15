package mouse.project.termverseweb.mouselib.provider;

import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.data.provider.ConnectionProvider;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.annotation.UseRestriction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@UseRestriction(usedBy = "test_data")
public class TestConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;
    @Auto
    public TestConnectionProvider() {
        this.dataSource = DataSourceTransfer.dataSource;
    }
    @Override
    public Connection provide() {
        try {
            if (dataSource == null) {
                throw new ORMException("Cannot set up data source!");
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
