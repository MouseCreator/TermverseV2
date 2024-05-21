package mouse.project.termverseweb.filters.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class KeycloakDataProviderBean implements KeycloakDataProvider {
    @Value("${KEYCLOAK_CLIENT_ID}")
    private String kcClientId;
    @Value("${KEYCLOAK_CLIENT_SECRET}")
    private String kcClientSecret;
    @Value("${KEYCLOAK_ADMIN_USERNAME}")
    private String kcAdminUsername;
    @Value("${KEYCLOAK_ADMIN_PASSWORD}")
    private String kcAdminPassword;
    @Override
    public KeycloakData provide() {
        KeycloakData instance = new KeycloakData();
        instance.setClientId(kcClientId);
        instance.setClientSecret(kcClientSecret);
        instance.setAdminUsername(kcAdminUsername);
        instance.setAdminPassword(kcAdminPassword);
        return instance;
    }
}
