package mouse.project.termverseweb.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import mouse.project.lib.web.filter.MFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

public class JWTFilter implements MFilter {

    @Override
    public boolean invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            Claims claims = null;

            String dbId = claims.get("db_id", String.class);
            String username = claims.getSubject();

            request.setAttribute("dbId", dbId);
            request.setAttribute("username", username);

        }
        return true;
    }
}
