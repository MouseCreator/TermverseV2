package mouse.project.termverseweb.filters.argument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.FilterException;
@Service
public class OptionalAuthorizationFactory {
    public OptionalAuthentication processTokenResponse(String jsonResponse){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (rootNode.path("active").asBoolean(true)) {
            String securityId = rootNode.path("sub").textValue();

            if (securityId != null) {
                return OptionalAuthentication.of(securityId);
            } else {
                throw new FilterException("Failed to extract one or both keys (username, databaseId).");
            }
        } else {
            return OptionalAuthentication.empty();
        }
    }
}
