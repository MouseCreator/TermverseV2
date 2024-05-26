package mouse.project.termverseweb.security.keys;

import lombok.Data;

@Data
public class KeycloakProperties {
    private String realm;
    private String clientId;
    private String authUrl;
    private String adminUsername;
    private String adminPassword;
}
