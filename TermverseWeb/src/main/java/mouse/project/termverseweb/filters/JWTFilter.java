package mouse.project.termverseweb.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.filter.MFilter;
import mouse.project.termverseweb.exception.FilterException;
import mouse.project.termverseweb.filters.helper.JWTFacade;
import mouse.project.termverseweb.filters.helper.TokenIntrospection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class JWTFilter implements MFilter {
    private final TokenIntrospection tokenIntrospection;

    public JWTFilter(TokenIntrospection tokenIntrospection) {
        this.tokenIntrospection = tokenIntrospection;
    }

    @Override
    public boolean invoke(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");

        if (token != null) {
            if(token.startsWith("Bearer ")) {
                token = token.substring("Bearer ".length());
            }
            String tokenDecoded = tokenIntrospection.decodeAndValidate(token);
            try {
                processTokenResponse(request, tokenDecoded);
            } catch (Exception e) {
                throw new StatusException(403, e.getMessage());
            }
        } else {
            throw new StatusException(403, "Authorization failed");
        }
        return true;
    }

    public static void processTokenResponse(HttpServletRequest request, String jsonResponse){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (rootNode.path("active").asBoolean(true)) {
            String preferredUsername = rootNode.path("username").textValue();
            long databaseId = rootNode.path("databaseId").asLong(-1);

            if (preferredUsername != null && databaseId != -1) {
                request.setAttribute("KC_USERNAME", preferredUsername);
                request.setAttribute("KC_DATABASE_ID", databaseId);
            } else {
                throw new FilterException("Failed to extract one or both keys (preferred_username, databaseId).");
            }
        } else {
            throw new FilterException("Token is not active or valid.");
        }
    }
}
