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
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import javax.crypto.SecretKey;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
public class JWTDecoder {

    private final KeycloakInvoker keycloakHelper;
    @Auto
    public JWTDecoder(KeycloakInvoker keycloakHelper) {
        this.keycloakHelper = keycloakHelper;
    }

    public Jws<Claims> decode(String jwt, PublicKey publicKey) {
        try {

            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(jwt);
        } catch (Exception e) {
            throw new FilterException(e);
        }
    }

    public PublicKey getPublicKey(String realm) {
        String keycloakPublicKey = keycloakHelper.getKeycloakPublicKey(realm);
        return extractPublicKeyFromJson(keycloakPublicKey);
    }


    private PublicKey extractPublicKeyFromJson(String json)  {
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
        try {
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(decoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(encodedKeySpec);
        } catch (Exception e) {
            throw new FilterException(e);
        }
    }
}
