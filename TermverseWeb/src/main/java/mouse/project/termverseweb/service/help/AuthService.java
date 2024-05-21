package mouse.project.termverseweb.service.help;

import mouse.project.termverseweb.exception.UnauthorizedException;
import mouse.project.termverseweb.exception.UnexpectedAuthTypeException;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.spring.OptionalAuthenticationToken;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.repository.UserMappingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger log = LogManager.getLogger(AuthService.class);
    private final UserMappingRepository userMappingRepository;
    public AuthService(UserMappingRepository userMappingRepository) {
        this.userMappingRepository = userMappingRepository;
    }
    public Long getUserId() {
        return getUser()
                .getId();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OptionalAuthenticationToken oaToken) {
            OptionalAuthentication optionalAuthentication = (OptionalAuthentication) oaToken.getPrincipal();
            if (optionalAuthentication.isEmpty()) {
                throw new UnauthorizedException("User is not logged in");
            }
            Optional<UserMapping> byId = userMappingRepository.findById(optionalAuthentication.getSecurityId());
            if (byId.isEmpty()) {
                throw new UnauthorizedException("User has no record in the database");
            }
            UserMapping userMapping = byId.get();
            return userMapping.getUser();
        }
        log.debug("Unexpected authentication: " + authentication);
        throw new UnexpectedAuthTypeException();
    }
}
