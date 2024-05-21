package mouse.project.termverseweb.config;

import mouse.project.termverseweb.security.KeyService;
import mouse.project.termverseweb.security.kc.KeycloakCommunication;
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
    private final KeycloakCommunication keycloakCommunication;
    private final KeyService keyService;
    @Autowired
    public SpringSecurityConfig(KeycloakCommunication keycloakCommunication, KeyService keyService) {
        this.keycloakCommunication = keycloakCommunication;
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

    private String getPublicKey() {
        return keycloakCommunication.getPublicKey();
    }

    private RSAPublicKey keyToUse() {
        String publicKey = getPublicKey();
        return convertToRSAPublicKey(publicKey);
    }

    private RSAPublicKey convertToRSAPublicKey(String publicKey) {
        return keyService.convertToRSAPublicKey(publicKey);
    }
}
