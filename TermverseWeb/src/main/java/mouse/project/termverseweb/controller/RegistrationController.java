package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.FromAttribute;
import mouse.project.lib.web.annotation.Get;
import mouse.project.lib.web.annotation.RBody;
import mouse.project.lib.web.annotation.URL;
import mouse.project.termverseweb.dto.register.UserRegisterDTO;
import mouse.project.termverseweb.dto.register.UserTokensDTO;
import mouse.project.termverseweb.exception.AlreadyAuthorizedException;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.resolver.CurrentUserContext;
import mouse.project.termverseweb.service.help.AuthContext;
import mouse.project.termverseweb.service.help.AuthService;
import mouse.project.termverseweb.service.register.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class RegistrationController {
    private final RegisterService registerService;
    private final AuthService authService;
    @Auto
    @Autowired
    public RegistrationController(RegisterService registerService, AuthService authService) {
        this.registerService = registerService;
        this.authService = authService;
    }

    @Get
    @URL("/register")
    public UserTokensDTO register(@RBody @RequestBody UserRegisterDTO userRegisterDTO,
                                  @CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        return doRegister(userRegisterDTO, optionalAuthentication);
    }
    private UserTokensDTO doRegister(UserRegisterDTO userRegisterDTO, Object authParam) {
        AuthContext authContext = authService.onAuth(authParam);
        if (authContext.isAuthenticated()) {
            throw new AlreadyAuthorizedException("User is already logged in");
        }
        return registerService.register(userRegisterDTO);
    }

    @Get
    @URL("/login")
    public UserTokensDTO login(@RBody @RequestBody UserRegisterDTO userRegisterDTO,
                               @CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        return doLogin(userRegisterDTO, optionalAuthentication);
    }

    private UserTokensDTO doLogin(UserRegisterDTO userRegisterDTO, Object authParam) {
        AuthContext authContext = authService.onAuth(authParam);
        if (authContext.isAuthenticated()) {
            throw new AlreadyAuthorizedException("User is already logged in");
        }
        return registerService.register(userRegisterDTO);
    }
}
