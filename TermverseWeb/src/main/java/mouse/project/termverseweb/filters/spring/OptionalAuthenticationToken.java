package mouse.project.termverseweb.filters.spring;

import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

public class OptionalAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 5713160538776318666L;
    private final OptionalAuthentication optionalAuthentication;

    public OptionalAuthenticationToken(OptionalAuthentication optionalAuthentication) {
        super(null);
        this.optionalAuthentication = optionalAuthentication;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return optionalAuthentication;
    }
}
