package mouse.project.termverseweb.filters.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.FilterException;

import javax.crypto.SecretKey;
import java.util.Base64;
@Service
public class JWTDecoder {

    private final KeycloakHelper keycloakHelper;
    @Auto
    public JWTDecoder(KeycloakHelper keycloakHelper) {
        this.keycloakHelper = keycloakHelper;
    }

    public Jws<Claims> decode(String jwt, String publicKey) {
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(publicKey));
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(jwt);
    }

    public String getPublicKey(String realm) {
        String keycloakPublicKey = keycloakHelper.getKeycloakPublicKey(realm);
        return extractPublicKeyFromJson(keycloakPublicKey);
    }


    private String extractPublicKeyFromJson(String json)  {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new FilterException(e);
        }


        JsonNode keyNode = rootNode.path("keys").get(0);
        JsonNode x5cNode = keyNode.get("x5c");
        String publicKeyPEM = x5cNode.get(0).asText();

        String publicKeyEncoded = publicKeyPEM.replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyEncoded);

        return new String(java.util.Base64.getMimeEncoder().encode(decoded));
    }
}
