package mouse.project.termverseweb.keys;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.util.HashMap;
import java.util.Map;

public class KeycloakConfigResolver implements org.keycloak.adapters.KeycloakConfigResolver {
    private KeycloakDeployment keycloakDeployment;
    private KeycloakPropertiesProvider provider;
    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        KeycloakProperties properties = provider.getProperties();
        if (keycloakDeployment != null) {
            return keycloakDeployment;
        }

        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(properties.getRealm());
        adapterConfig.setAuthServerUrl(properties.getAuthUrl());
        adapterConfig.setSslRequired("external");
        adapterConfig.setResource(properties.getClientId());

        Map<String, Object> credentials = getCredentialsFromEnv();
        adapterConfig.setCredentials(credentials);

        keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig);
        return keycloakDeployment;
    }

    private Map<String, Object> getCredentialsFromEnv() {
        Map<String, Object> credentials = new HashMap<>();
        String clientSecret = System.getenv("KEYCLOAK_CLIENT_SECRET");
        if (clientSecret != null) {
            credentials.put("secret", clientSecret);
        }
        return credentials;
    }
}
