package mouse.project.termverseweb.service.help;

import mouse.project.termverseweb.exception.UnauthorizedException;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.repository.UserMappingRepository;

import java.util.Optional;

public class OptionalAuthContext implements AuthContext {

    private final OptionalAuthentication optionalAuthentication;
    private final UserMappingRepository userMappingRepository;
    public OptionalAuthContext(OptionalAuthentication optionalAuthentication,
                               UserMappingRepository userMappingRepository) {
        this.optionalAuthentication = optionalAuthentication;
        this.userMappingRepository = userMappingRepository;
    }

    @Override
    public Long toUserId() {
        return toUser().getId();
    }

    @Override
    public User toUser() {
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

    @Override
    public boolean isAuthorized() {
        return optionalAuthentication.isPresent();
    }

    @Override
    public OptionalAuthentication getOptionalAuthentication() {
        return optionalAuthentication;
    }
}
