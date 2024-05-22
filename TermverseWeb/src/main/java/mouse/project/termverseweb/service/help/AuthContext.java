package mouse.project.termverseweb.service.help;

import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.model.User;

public interface AuthContext {
    Long toUserId();
    User toUser();
    boolean isAuthenticated();
    OptionalAuthentication getOptionalAuthentication();
}
