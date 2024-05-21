package mouse.project.termverseweb.security;

import mouse.project.lib.web.parse.JacksonBodyParser;
import mouse.project.termverseweb.exception.KeyConvertException;
import mouse.project.termverseweb.security.kc.JWKSResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
@Service
public class KeyService {
    public RSAPublicKey convertToRSAPublicKey(String json) {
        JacksonBodyParser jacksonBodyParser = new JacksonBodyParser();
        JWKSResponse parsedResponse = jacksonBodyParser.parse(json, JWKSResponse.class);
        if (parsedResponse.getKeys() == null || parsedResponse.getKeys().isEmpty()) {
            throw new KeyConvertException("Failed to fetch JWKS from Keycloak");
        }
        JWKSResponse.JwksKey keyData = parsedResponse.getKeys().stream()
                .filter(key -> key.getUse().equals("sig"))
                .findFirst()
                .orElseThrow(() -> new KeyConvertException("No suitable key found in JWKS"));
        try {
            return convertToRSAPublicKey(keyData.getN(), keyData.getE());
        } catch (Exception e) {
            throw new KeyConvertException("Cannot convert public key: " + json, e);
        }
    }

    private RSAPublicKey convertToRSAPublicKey(String n, String e) throws Exception {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(n);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(e);

        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return (RSAPublicKey) publicKey;
    }
}
