package mouse.project.termverseweb.keys;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.tomcat.ContextConfigurator;
import org.apache.catalina.Context;
import org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve;
@Service
public class SecurityContextConfiguration implements ContextConfigurator {
    @Override
    public void config(Context context) {
        KeycloakAuthenticatorValve keycloakAuthenticatorValve = new KeycloakAuthenticatorValve();
        context.getPipeline().addValve(keycloakAuthenticatorValve);

        System.setProperty("keycloak.config.resolver", KeycloakConfigResolver.class.getName());
    }
}
