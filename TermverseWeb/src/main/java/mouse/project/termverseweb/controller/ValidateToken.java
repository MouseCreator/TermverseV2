package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.valid.ValidationDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;

@Controller
public class ValidateToken {

    @Get
    @URL("/validate")
    public ValidationDTO create(
            @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        if (optionalAuthentication.isEmpty()) {
            return new ValidationDTO("token-invalid");
        }
        return new ValidationDTO("token-valid");
    }
}
