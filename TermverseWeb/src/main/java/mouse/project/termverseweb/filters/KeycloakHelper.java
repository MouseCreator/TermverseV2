package mouse.project.termverseweb.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KeycloakHelper {
    public static String getKeycloakPublicKey(String realm) {
        String url = "http://localhost:8180/realms/" + realm + "/protocol/openid-connect/certs";
        System.out.println("Requesting " + url);
        HttpURLConnection connection = null;
        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println("GET request not worked, Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String extractPublicKeyFromJson(String json)  {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Assuming the first key in the array is the one we want
        JsonNode keyNode = rootNode.path("keys").get(0);
        JsonNode x5cNode = keyNode.get("x5c");
        String publicKeyPEM = x5cNode.get(0).asText();// x5c contains the PEM certificate chain

        // Base64 decode the first certificate in the chain
        String publicKeyEncoded = publicKeyPEM.replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replaceAll("\\s", "");  // remove CRLF

        byte[] decoded = Base64.getDecoder().decode(publicKeyEncoded);

        return new String(java.util.Base64.getMimeEncoder().encode(decoded));
    }
    public static void main(String[] args) {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.WOjYquz2aYE_rpCZwgtxs9dIIn8tR4ShjY27pnaOQq0";
        String realm = "termverse";
        String publicKeyJson = getKeycloakPublicKey(realm);
        System.out.println("JSON:" + publicKeyJson);
        String publicKey = extractPublicKeyFromJson(publicKeyJson);
        System.out.println(publicKey);
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt);

        String name = claimsJws.getPayload().get("name", String.class);

        System.out.println(name);
    }
}
