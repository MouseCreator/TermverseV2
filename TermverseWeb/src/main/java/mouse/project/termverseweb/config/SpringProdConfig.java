package mouse.project.termverseweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import mouse.project.termverseweb.filters.helper.KeycloakDataProvider;
import mouse.project.termverseweb.security.TokenService;
import mouse.project.termverseweb.security.TokenServiceImpl;
import mouse.project.termverseweb.security.kc.KeycloakState;
import mouse.project.termverseweb.service.register.KeycloakClient;
import mouse.project.termverseweb.service.register.KeycloakClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class SpringProdConfig {
    @Bean
    public KeycloakClient keycloakClient(ObjectMapper objectMapper, KeycloakDataProvider keycloakDataProvider) {
        return new KeycloakClientImpl(objectMapper, keycloakDataProvider);
    }

    @Bean
    public TokenService tokenService(KeycloakState keycloakState) {
        return new TokenServiceImpl(keycloakState);
    }
}
