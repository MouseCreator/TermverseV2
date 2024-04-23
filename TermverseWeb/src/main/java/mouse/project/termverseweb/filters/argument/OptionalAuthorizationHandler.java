package mouse.project.termverseweb.filters.argument;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.repository.UserMappingRepository;
import mouse.project.termverseweb.repository.UserRepository;

import java.util.Optional;

@Service
public class OptionalAuthorizationHandler {
    private final UserMappingRepository mappingRepository;

    private final UserRepository userRepository;
    @Auto
    public OptionalAuthorizationHandler(UserMappingRepository mappingRepository, UserRepository repository) {
        this.mappingRepository = mappingRepository;
        userRepository = repository;
    }

    public User toUser(OptionalAuthentication oAuth) {
        if (oAuth.isEmpty()) {
            throw new StatusException(401);
        }
        Optional<UserMapping> byId = mappingRepository.findById(oAuth.getSecurityId());
        if (byId.isEmpty()) {
            throw new ControllerException("User is not registered to the system: " + oAuth.getSecurityId());
        }
        return byId.get().getUser();

    }
    public Long toUserId(OptionalAuthentication oAuth) {
        return toUser(oAuth).getId();
    }

    public void registerUser(OptionalAuthentication oAuth, Long userId) {
        if (oAuth.isEmpty()) {
            throw new ControllerException("Cannot register user! Optional Authorization is empty");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ControllerException("Cannot register user! User not found! For " + oAuth.getSecurityId());
        }
        mappingRepository.save(new UserMapping(oAuth.getSecurityId(), user.get()));
    }
}
