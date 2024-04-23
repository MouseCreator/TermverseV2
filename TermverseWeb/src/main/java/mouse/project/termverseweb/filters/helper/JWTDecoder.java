package mouse.project.termverseweb.filters.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.FilterException;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JWTDecoder {

    private final KeycloakInvoker keycloakHelper;
    @Auto
    public JWTDecoder(KeycloakInvoker keycloakHelper) {
        this.keycloakHelper = keycloakHelper;
    }

    public Jws<Claims> decode(String jwt, String stringPublicKey) {
        try {
            stringPublicKey = stringPublicKey.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(stringPublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(jwt);
            System.out.println(decodedJWT);
            return null;
        } catch (Exception e) {
            throw new FilterException(e);
        }
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
        try {
            return new String(Base64.getEncoder().encode(decoded));
        } catch (Exception e) {
            throw new FilterException(e);
        }
    }
}
