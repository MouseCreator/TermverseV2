package mouse.project.termverseweb.filters;

import jakarta.servlet.http.Cookie;
import mouse.project.lib.ioc.annotation.Service;
import org.springframework.stereotype.Component;

@Service
@Component
public class JWTHelper {
    public String getAuthToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("termverse_access_token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
