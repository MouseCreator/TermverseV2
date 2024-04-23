package mouse.project.termverseweb.filters.helper;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.FilterException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
@Service
public class KeycloakHelper implements KeycloakInvoker {

    public String getKeycloakPublicKey(String realm) {
        String url = "http://localhost:8180/realms/" + realm + "/protocol/openid-connect/certs";
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
                throw new FilterException("GET request not worked, Response Code: " + responseCode);
            }
        } catch (Exception e) {
            throw new FilterException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
