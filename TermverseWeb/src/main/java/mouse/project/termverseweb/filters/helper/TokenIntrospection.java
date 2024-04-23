package mouse.project.termverseweb.filters.helper;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.FilterException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Base64;
@Service
public class TokenIntrospection {
    private final KeycloakDataProvider provider;
    @Auto
    public TokenIntrospection(KeycloakDataProvider provider) {
        this.provider = provider;
    }

    public String decodeAndValidate(String token) {
        KeycloakData data = provider.provide();
        String clientId = data.getClientId();
        String clientSecret = data.getClientSecret();
        String introspectionUrl = "http://localhost:8180/realms/termverse/protocol/openid-connect/token/introspect";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(introspectionUrl);
            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            post.setHeader("Authorization", "Basic " + encodedAuth);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            StringEntity entity = new StringEntity("token=" + token + "&token_type_hint=access_token");
            post.setEntity(entity);

            return EntityUtils.toString(client.execute(post).getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
