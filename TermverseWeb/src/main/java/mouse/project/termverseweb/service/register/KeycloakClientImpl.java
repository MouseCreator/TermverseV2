package mouse.project.termverseweb.service.register;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.dto.register.UserRegisterDTO;
import mouse.project.termverseweb.filters.helper.KeycloakData;
import mouse.project.termverseweb.filters.helper.KeycloakDataProvider;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class KeycloakClientImpl implements KeycloakClient{
    private final ObjectMapper objectMapper;
    private final KeycloakDataProvider dataProvider;
    @Auto
    public KeycloakClientImpl(ObjectMapper objectMapper, KeycloakDataProvider keycloakDataProvider) {
        this.objectMapper = objectMapper;
        this.dataProvider = keycloakDataProvider;
    }

    public Tokens registerUser(UserRegisterDTO userRegisterDTO) throws Exception {
        KeycloakData data = dataProvider.provide();
        String adminUsername = data.getAdminUsername();
        String adminPassword = data.getAdminPassword();
        String clientSecret = data.getClientSecret();

        Tokens userTokensData;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String adminToken = getAdminToken(httpClient, adminUsername, adminPassword);

            String username = userRegisterDTO.getLogin();
            String password = userRegisterDTO.getPassword();

            createUser(httpClient, adminToken, username, password);
            userTokensData = getUserTokens(httpClient, username, password, clientSecret);
        }
        return userTokensData;
    }

    public Tokens loginUser(UserRegisterDTO userRegisterDTO) throws Exception {
        KeycloakData data = dataProvider.provide();
        String clientSecret = data.getClientSecret();
        String login = userRegisterDTO.getLogin();
        String password = userRegisterDTO.getPassword();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return getUserTokens(httpClient, login, password, clientSecret);
        }
    }

    private String getAdminToken(CloseableHttpClient httpClient, String username, String password) throws Exception {
        HttpPost post = new HttpPost("http://localhost:8180/realms/master/protocol/openid-connect/token");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity entity = new StringEntity(
                "username=" + username + "&password=" + password + "&grant_type=password&client_id=admin-cli",
                ContentType.APPLICATION_FORM_URLENCODED
        );
        post.setEntity(entity);

        HttpResponse response = httpClient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);
        JsonNode jsonResponse = objectMapper.readTree(responseString);

        return jsonResponse.get("access_token").asText();
    }

    private void createUser(CloseableHttpClient httpClient, String token, String username, String password) throws Exception {
        HttpPost post = new HttpPost("http://localhost:8180/admin/realms/termverse/users");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Authorization", "Bearer " + token);

        ObjectNode userJson = objectMapper.createObjectNode();
        userJson.put("username", username);
        userJson.put("enabled", true);
        userJson.put("email", username + "@mail.com");
        userJson.put("emailVerified", true);

        ObjectNode credentials = objectMapper.createObjectNode();
        credentials.put("type", "password");
        credentials.put("value", password);
        credentials.put("temporary", false);

        ArrayNode credentialsArray = objectMapper.createArrayNode();
        credentialsArray.add(credentials);

        userJson.set("credentials", credentialsArray);

        StringEntity entity = new StringEntity(userJson.toString(), ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        HttpResponse response = httpClient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        EntityUtils.consume(responseEntity);
    }
    private Tokens getUserTokens(CloseableHttpClient httpClient, String username, String password, String clientSecret) throws Exception {
        HttpPost post = new HttpPost("http://localhost:8180/realms/termverse/protocol/openid-connect/token");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        StringEntity entity = new StringEntity(
                "client_id=termverse-app&username=" + username + "&password=" + password + "&grant_type=password&client_secret=" + clientSecret,
                ContentType.APPLICATION_FORM_URLENCODED
        );
        post.setEntity(entity);

        HttpResponse response = httpClient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        String accessToken = jsonResponse.get("access_token").asText();
        String refreshToken = jsonResponse.get("refresh_token").asText();

        return new Tokens(accessToken, refreshToken);
    }

    public String fetchKeycloakKeys() throws Exception {
        String publicKeyUrl = "http://localhost:8180/realms/termverse/protocol/openid-connect/certs";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(publicKeyUrl);
            HttpEntity entity = client.execute(post).getEntity();
            return EntityUtils.toString(entity);
        }
    }
}
