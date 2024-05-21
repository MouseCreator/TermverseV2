package mouse.project.termverseweb.security.kc;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.exception.FilterException;
import mouse.project.termverseweb.security.KeyService;
import mouse.project.termverseweb.service.register.KeycloakClient;
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
    private final KeycloakClient keycloakClient;
    @Auto
    @Autowired
    public KeycloakState(KeyService keyService, KeycloakClient keycloakClient) {
        this.keyService = keyService;
        this.keycloakClient = keycloakClient;
    }

    private String fetchKeycloakKeys() {
        try {
            return keycloakClient.fetchKeycloakKeys();
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
