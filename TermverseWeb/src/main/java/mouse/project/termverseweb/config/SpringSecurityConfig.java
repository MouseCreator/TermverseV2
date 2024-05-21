package mouse.project.termverseweb.config;

import mouse.project.termverseweb.filters.spring.OptionalAuthenticationFilter;
import mouse.project.termverseweb.security.kc.KeycloakState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SpringSecurityConfig {
    private final KeycloakState keycloakState;
    @Autowired
    public SpringSecurityConfig(KeycloakState keycloakState) {
        this.keycloakState = keycloakState;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtDecoder jwtDecoder,
                                           OptionalAuthenticationFilter optionalAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .addFilterBefore(optionalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            ;
        return http.build();
    }
    @Bean
    public OptionalAuthenticationFilter optionalAuthenticationFilter(JwtDecoder jwtDecoder) {
        return new OptionalAuthenticationFilter(jwtDecoder);
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyToUse()).build();
    }

    private RSAPublicKey keyToUse() {
        return keycloakState.getPublicKey();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE", "PUT", "UPDATE"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "Content-Length", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
