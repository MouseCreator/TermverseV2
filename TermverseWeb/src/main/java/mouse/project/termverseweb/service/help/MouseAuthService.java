package mouse.project.termverseweb.service.help;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;

@Service
@org.springframework.stereotype.Service
public class MouseAuthService implements AuthService {

    private final AuthContextService authContextService;

    public MouseAuthService(AuthContextService authContextService) {
        this.authContextService = authContextService;
    }

    @Override
    public AuthContext onAuth(Object param) {
        if (! (param instanceof OptionalAuthentication optA)) {
            throw new UnsupportedOperationException();
        }
        return authContextService.provideContext(optA);
    }
}
