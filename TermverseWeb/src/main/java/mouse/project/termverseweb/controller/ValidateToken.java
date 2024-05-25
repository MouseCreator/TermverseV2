package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.valid.ValidationDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.resolver.CurrentUserContext;
import mouse.project.termverseweb.service.help.AuthService;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class ValidateToken {
    private final AuthService authService;

    public ValidateToken(AuthService authService) {
        this.authService = authService;
    }

    @Get
    @URL("/validate")
    @GetMapping("/validate")
    public ValidationDTO create(@CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        return doCreate(optionalAuthentication);
    }

    private ValidationDTO doCreate(Object authObject) {
        boolean authenticated = authService.onAuth(authObject).isAuthenticated();
        if (authenticated) {
            return new ValidationDTO("token-valid");
        }
        return new ValidationDTO("token-invalid");
    }

}
