package mouse.project.termverseweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.TokenValidationException;
import mouse.project.termverseweb.security.kc.KeycloakState;
import java.security.interfaces.RSAPublicKey;
@Service
public class TokenServiceImpl implements TokenService {
    private final KeycloakState keycloakState;
    @Auto
    public TokenServiceImpl(KeycloakState keycloakState) {
        this.keycloakState = keycloakState;
    }

    public String getSubject(String token) {
        RSAPublicKey publicKey = keycloakState.getPublicKey();
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getPayload().getSubject();
        } catch (Exception e) {
            throw new TokenValidationException("Failed to validate token", e);
        }
    }
}
