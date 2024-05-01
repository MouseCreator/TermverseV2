package mouse.project.termverseweb.filters;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.filter.MFilter;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationFactory;
import mouse.project.termverseweb.filters.helper.TokenIntrospection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class JWTFilter implements MFilter {
    private final TokenIntrospection tokenIntrospection;
    private final OptionalAuthorizationFactory optionalAuthorizationFactory;
    @Auto
    public JWTFilter(TokenIntrospection tokenIntrospection, OptionalAuthorizationFactory optionalAuthorizationFactory) {
        this.tokenIntrospection = tokenIntrospection;
        this.optionalAuthorizationFactory = optionalAuthorizationFactory;
    }

    @Override
    public boolean invoke(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = getAuthToken(cookies);
        if (token != null) {
            if(token.startsWith("Bearer ")) {
                token = token.substring("Bearer ".length());
            }
            String tokenDecoded = tokenIntrospection.decodeAndValidate(token);
            try {
                OptionalAuthentication optionalAuthentication = optionalAuthorizationFactory.processTokenResponse(tokenDecoded);
                request.setAttribute(Args.OPT_AUTH, optionalAuthentication);
            } catch (Exception e) {
                throw new StatusException(403, e.getMessage());
            }
        } else {
            request.setAttribute(Args.OPT_AUTH, OptionalAuthentication.empty());
        }
        return true;
    }

    private String getAuthToken(Cookie[] cookies) {
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
