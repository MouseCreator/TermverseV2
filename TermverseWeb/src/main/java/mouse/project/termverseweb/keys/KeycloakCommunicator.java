package mouse.project.termverseweb.keys;

import jakarta.ws.rs.core.Response;
import mouse.project.lib.ioc.annotation.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
@Service
public class KeycloakCommunicator {

    private final static Logger log = LogManager.getLogger(KeycloakCommunicator.class);
    private KeycloakPropertiesProvider provider;
    public void registerUser(UserData userData) {
        KeycloakProperties properties = provider.getProperties();
        try (Keycloak keycloak = Keycloak.getInstance(
                properties.getAuthUrl(), // Keycloak URL
                properties.getRealm(),                        // Realm
                properties.getAdminUsername(),                   // Admin username
                properties.getAdminPassword(),                   // Admin password
                "admin-cli"                                   // Admin client ID
        )) {
            UsersResource usersResource = keycloak.realm("yourRealm").users();

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userData.getPassword());
            credential.setTemporary(false);

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userData.getUsername());
            user.setEmail(userData.getEmail());
            user.setEnabled(true);
            user.setCredentials(List.of(credential));

            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                log.debug("Created User ID: " + userId);
            } else {
                log.debug("Failed to create user, status: " + response.getStatus());
            }
        }
    }

    public String obtainAccessToken(String username, String password) {
        KeycloakProperties properties = provider.getProperties();
        try (Keycloak keycloak = Keycloak.getInstance(
                properties.getAuthUrl(), //url
                properties.getRealm(), //realm
                username, //username
                password, //password
                properties.getClientId() //clientId
        )) {
            AccessTokenResponse response = keycloak.tokenManager().getAccessToken();
            return response.getToken();
        }

    }
}
