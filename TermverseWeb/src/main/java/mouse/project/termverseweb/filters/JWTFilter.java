package mouse.project.termverseweb.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.filter.MFilter;
import mouse.project.termverseweb.filters.helper.JWTFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class JWTFilter implements MFilter {
    private final JWTFacade jwtFacade;
    @Auto
    public JWTFilter(JWTFacade jwtFacade) {
        this.jwtFacade = jwtFacade;
    }

    @Override
    public boolean invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            Jws<Claims> claims = jwtFacade.getClaims(token);
            processClaims(request, claims);
        } else {
            response.setStatus(403);
            throw new ServletException("Authorization failed");
        }
        return true;
    }

    private void processClaims(HttpServletRequest request, Jws<Claims> claims) {
        Claims payload = claims.getPayload();
        request.setAttribute("jwt_db_id", payload.get("databaseId"));
        request.setAttribute("jwt_username", payload.getSubject());
    }
}
