package mouse.project.termverseweb.mouselib.provider;

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
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
