package mouse.project.termverseweb.mouse;

import mouse.project.lib.data.provider.ConnectionProvider;

import java.sql.Connection;

public class CustomConnectionProvider implements ConnectionProvider {
    @Override
    public Connection provide() {
        return null;
    }
}
