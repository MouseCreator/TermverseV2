package mouse.project.termverseweb.security.kc;

import mouse.project.termverseweb.exception.FilterException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class KeycloakCommunication {
    private String publicKey = null;
    private String fetchKeycloakPublicKey() {
        String publicKeyUrl = "http://localhost:8180/realms/termverse/protocol/openid-connect/certs";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(publicKeyUrl);
            HttpEntity entity = client.execute(post).getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new FilterException(e);
        }
    }

    public String getPublicKey() {
        if (publicKey == null) {
            publicKey = fetchKeycloakPublicKey();
        }
        return publicKey;
    }
}
