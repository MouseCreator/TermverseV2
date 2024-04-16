package mouse.project.termverseweb.mouse;

import mouse.project.lib.data.provider.ConnectionProvider;
import mouse.project.lib.files.PropertyMap;
import mouse.project.lib.files.ReadPropertiesFile;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.annotation.UseRestriction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@UseRestriction(usedBy = "main-app")
public class CustomConnectionProvider implements ConnectionProvider {

    private final ReadPropertiesFile readPropertiesFile;

    private DBCredentials credentials;
    @Auto
    public CustomConnectionProvider(ReadPropertiesFile readPropertiesFile) {
        this.readPropertiesFile = readPropertiesFile;
    }

    private record DBCredentials(String url, String user, String password) {
    }

    @After
    public void onSetup() {
        PropertyMap propertyMap = readPropertiesFile.readFile("mouse.properties");

        String url = propertyMap.getPropertyValue("database.url");
        String user = propertyMap.getPropertyValue("database.user");
        String password = propertyMap.getPropertyValue("database.password");
        String driver = propertyMap.getPropertyValue("database.driver");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        credentials = new DBCredentials(url, user, password);
    }
    @Override
    public Connection provide() {
        try {
            return DriverManager.getConnection(credentials.url(), credentials.user(), credentials.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
