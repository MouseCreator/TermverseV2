package mouse.project.termverseweb.filters.argument;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.exception.MissingEntityException;
import mouse.project.termverseweb.exception.UnauthorizedException;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.repository.UserMappingRepository;

import java.util.Optional;

@Service
public class OptionalAuthorizationHandler {
    private final UserMappingRepository mappingRepository;
    @Auto
    public OptionalAuthorizationHandler(UserMappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    public User toUser(OptionalAuthentication oAuth) {
        if (oAuth.isEmpty()) {
            throw new UnauthorizedException("User is not authorized");
        }
        Optional<UserMapping> byId = mappingRepository.findById(oAuth.getSecurityId());
        if (byId.isEmpty()) {
            throw new MissingEntityException("User is not registered to the system: " + oAuth.getSecurityId());
        }
        return byId.get().getUser();

    }
    public Long toUserId(OptionalAuthentication oAuth) {
        return toUser(oAuth).getId();
    }
}
