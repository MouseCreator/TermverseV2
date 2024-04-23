package mouse.project.termverseweb.filters.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JWTFacade {
    Jws<Claims> getClaims(String jwtToken);
}
