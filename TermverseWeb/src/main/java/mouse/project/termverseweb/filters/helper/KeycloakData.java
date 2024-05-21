package mouse.project.termverseweb.filters.helper;

import lombok.Data;

@Data
public class KeycloakData {
    private String clientId;
    private String clientSecret;
    private String adminUsername;
    private String adminPassword;
}
