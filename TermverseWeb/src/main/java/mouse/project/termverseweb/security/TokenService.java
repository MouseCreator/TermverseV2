package mouse.project.termverseweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.exception.TokenValidationException;
import mouse.project.termverseweb.security.kc.KeycloakState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;

@Service
@mouse.project.lib.ioc.annotation.Service
public class TokenService {

    private final KeyService keyService;
    private final KeycloakState keycloakState;
    @Auto
    @Autowired
    public TokenService(KeyService keyService, KeycloakState keycloakState) {
        this.keyService = keyService;
        this.keycloakState = keycloakState;
    }

    public Claims getPayload(String token) {
        RSAPublicKey publicKey = keycloakState.getPublicKey();
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getPayload();
        } catch (Exception e) {
            throw new TokenValidationException("Failed to validate token", e);
        }
    }
}
