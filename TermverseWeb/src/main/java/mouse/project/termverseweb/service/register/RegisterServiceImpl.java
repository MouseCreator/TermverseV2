package mouse.project.termverseweb.service.register;

import jakarta.transaction.Transactional;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.dto.register.UserRegisterDTO;
import mouse.project.termverseweb.dto.register.UserTokensDTO;
import mouse.project.termverseweb.exception.NoSuchUserException;
import mouse.project.termverseweb.exception.RegisterException;
import mouse.project.termverseweb.exception.UserAlreadyExistsException;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.repository.UserMappingRepository;
import mouse.project.termverseweb.security.TokenService;
import mouse.project.termverseweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
@org.springframework.stereotype.Service
public class RegisterServiceImpl implements RegisterService {
    private final KeycloakClient keycloakClient;
    private final TokenService tokenService;
    private final UserService userService;
    private final UserMappingRepository userMapping;
    @Auto
    @Autowired
    public RegisterServiceImpl(KeycloakClient keycloakClient,
                               TokenService tokenService,
                               UserService userService,
                               UserMappingRepository userMapping) {
        this.keycloakClient = keycloakClient;
        this.tokenService = tokenService;
        this.userService = userService;
        this.userMapping = userMapping;
    }

    @Override
    @Transactional
    public UserTokensDTO register(UserRegisterDTO registerDTO) {
        String login = registerDTO.getLogin();
        if (userService.existsByName(login)) {
            throw new UserAlreadyExistsException("User with login " + login + " already exists");
        }
        try {
            Tokens tokens = keycloakClient.registerUser(registerDTO);
            String securityId = tokenService.getSubject(tokens.getAccessToken());
            User saved = userService.saveUser(login);
            userMapping.save(new UserMapping(securityId, saved));
            return new UserTokensDTO(saved.getId(), tokens.getAccessToken(), tokens.getRefreshToken());
        } catch (Exception e) {
            throw new RegisterException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserTokensDTO login(UserRegisterDTO registerDTO) {
        String login = registerDTO.getLogin();
        if (!userService.existsByName(login)) {
            throw new NoSuchUserException("User not found by login: " + login);
        }
        Tokens tokens;
        try {
            tokens = keycloakClient.loginUser(registerDTO);
        } catch (Exception e) {
            throw new RegisterException(e.getMessage());
        }
        String securityId = tokenService.getSubject(tokens.getAccessToken());
        Optional<UserMapping> byId = userMapping.findById(securityId);
        if (byId.isEmpty()) {
            throw new NoSuchUserException("Cannot find user mapping by security id: " + securityId);
        }
        return new UserTokensDTO(byId.get().getUser().getId(), tokens.getAccessToken(), tokens.getRefreshToken());

    }
}
