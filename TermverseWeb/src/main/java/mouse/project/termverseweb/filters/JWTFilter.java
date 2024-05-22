package mouse.project.termverseweb.filters;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.filter.MFilter;
import mouse.project.termverseweb.exception.TokenValidationException;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationFactory;
import mouse.project.termverseweb.security.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mouse.project.termverseweb.service.help.AuthContextService;

@Service
public class JWTFilter implements MFilter {
    private final TokenService tokenService;
    private final OptionalAuthorizationFactory optionalAuthorizationFactory;
    @Auto
    public JWTFilter(TokenService tokenService,
                     OptionalAuthorizationFactory optionalAuthorizationFactory,
                     AuthContextService authContextService) {
        this.tokenService = tokenService;
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
            try {
                String subject = tokenService.getSubject(token);
                OptionalAuthentication optionalAuthentication = optionalAuthorizationFactory.fromSubject(subject);
                request.setAttribute(Args.OPT_AUTH, optionalAuthentication);
            } catch (TokenValidationException e) {
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
