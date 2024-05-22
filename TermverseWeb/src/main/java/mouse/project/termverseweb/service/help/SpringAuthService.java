package mouse.project.termverseweb.service.help;

import mouse.project.termverseweb.exception.UnexpectedAuthTypeException;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.spring.OptionalAuthenticationToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SpringAuthService implements AuthService{
    private final Logger log = LogManager.getLogger(AuthService.class);
    private final AuthContextService authContextService;
    public SpringAuthService(AuthContextService authContextService) {
        this.authContextService = authContextService;
    }
    public AuthContext onAuth(Object param) {
        if (! (param instanceof Authentication)) {
            throw new UnexpectedAuthTypeException();
        }
        Authentication authentication = (Authentication) param;
        if (authentication instanceof OptionalAuthenticationToken oaToken) {
            OptionalAuthentication optionalAuthentication = (OptionalAuthentication) oaToken.getPrincipal();
            return authContextService.provideContext(optionalAuthentication);
        }
        log.debug("Unexpected authentication: " + authentication);
        throw new UnexpectedAuthTypeException();
    }
}
