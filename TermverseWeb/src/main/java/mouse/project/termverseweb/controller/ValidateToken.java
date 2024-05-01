package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;

@Controller
public class ValidateToken {

    @Get
    @URL("/validate")
    public String create(
            @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        if (optionalAuthentication.isEmpty()) {
            return "token-invalid";
        }
        return "token-valid";
    }
}
