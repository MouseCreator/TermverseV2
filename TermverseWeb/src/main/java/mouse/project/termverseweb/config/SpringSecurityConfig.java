package mouse.project.termverseweb.config;

import mouse.project.termverseweb.security.KeyService;
import mouse.project.termverseweb.security.kc.KeycloakState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final KeycloakState keycloakState;
    private final KeyService keyService;
    @Autowired
    public SpringSecurityConfig(KeycloakState keycloakState, KeyService keyService) {
        this.keycloakState = keycloakState;
        this.keyService = keyService;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyToUse()).build();
    }

    private RSAPublicKey keyToUse() {
        return keycloakState.getPublicKey();
    }

}
