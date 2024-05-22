package mouse.project.termverseweb.filters.helper;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class KeycloakDataProviderMock implements KeycloakDataProvider {
    @Override
    public KeycloakData provide() {
        throw new UnsupportedOperationException();
    }
}
