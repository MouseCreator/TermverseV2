package mouse.project.termverseweb.filters;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.lib.web.filter.MFilter;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthorization;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationFactory;
import mouse.project.termverseweb.filters.helper.TokenIntrospection;

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
        String token = request.getHeader("Authorization");

        if (token != null) {
            if(token.startsWith("Bearer ")) {
                token = token.substring("Bearer ".length());
            }
            String tokenDecoded = tokenIntrospection.decodeAndValidate(token);
            try {
                OptionalAuthorization optionalAuthorization = optionalAuthorizationFactory.processTokenResponse(tokenDecoded);
                request.setAttribute(Args.OPT_AUTH, optionalAuthorization);
            } catch (Exception e) {
                throw new StatusException(403, e.getMessage());
            }
        } else {
            request.setAttribute(Args.OPT_AUTH, OptionalAuthorization.empty());
        }
        return true;
    }


}
