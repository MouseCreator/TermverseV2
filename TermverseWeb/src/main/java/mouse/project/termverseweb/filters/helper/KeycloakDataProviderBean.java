package mouse.project.termverseweb.filters.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakDataProviderBean implements KeycloakDataProvider {
    @Value("${KC_CLIENT_ID}")
    private String kcClientId;
    @Value("${KC_CLIENT_SECRET}")
    private String kcClientSecret;
    @Value("${KC_ADMIN_USERNAME}")
    private String kcAdminUsername;
    @Value("${KC_ADMIN_PASSWORD}")
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
