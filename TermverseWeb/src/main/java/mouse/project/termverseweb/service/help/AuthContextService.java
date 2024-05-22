package mouse.project.termverseweb.service.help;

import mouse.project.termverseweb.filters.argument.OptionalAuthentication;

public interface AuthContextService {
    AuthContext provideContext(OptionalAuthentication optionalAuthentication);
}
