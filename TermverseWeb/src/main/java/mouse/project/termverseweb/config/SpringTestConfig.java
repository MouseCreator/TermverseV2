package mouse.project.termverseweb.config;
import mouse.project.termverseweb.security.TokenService;
import mouse.project.termverseweb.security.TokenServiceMock;
import mouse.project.termverseweb.service.register.KeycloakClient;
import mouse.project.termverseweb.service.register.KeycloakMockImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class SpringTestConfig {
    @Bean
    public KeycloakClient keycloakClient() {
        return new KeycloakMockImplementation();
    }
    @Bean
    public TokenService tokenService() {
        return new TokenServiceMock();
    }
}
