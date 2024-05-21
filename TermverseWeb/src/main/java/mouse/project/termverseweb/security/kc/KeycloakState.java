package mouse.project.termverseweb.security.kc;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.exception.FilterException;
import mouse.project.termverseweb.security.KeyService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;

@Service
@mouse.project.lib.ioc.annotation.Service
public class KeycloakState {
    private RSAPublicKey publicKey = null;
    private final KeyService keyService;
    @Auto
    @Autowired
    public KeycloakState(KeyService keyService) {
        this.keyService = keyService;
    }

    private String fetchKeycloakKeys() {
        String publicKeyUrl = "http://localhost:8180/realms/termverse/protocol/openid-connect/certs";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(publicKeyUrl);
            HttpEntity entity = client.execute(post).getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new FilterException(e);
        }
    }

    public RSAPublicKey getPublicKey() {
        if (publicKey == null) {
            String keysJson = fetchKeycloakKeys();
            publicKey = keyService.convertToRSAPublicKey(keysJson);
        }
        return publicKey;
    }
}
