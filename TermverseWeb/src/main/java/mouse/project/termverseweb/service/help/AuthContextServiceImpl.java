package mouse.project.termverseweb.service.help;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.repository.UserMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@org.springframework.stereotype.Service
public class AuthContextServiceImpl implements AuthContextService {
    private final UserMappingRepository userMappingRepository;
    @Auto
    @Autowired
    public AuthContextServiceImpl(UserMappingRepository userMappingRepository) {
        this.userMappingRepository = userMappingRepository;
    }

    @Override
    public AuthContext provideContext(OptionalAuthentication optionalAuthentication) {
        return new OptionalAuthContext(optionalAuthentication, userMappingRepository);
    }
}
