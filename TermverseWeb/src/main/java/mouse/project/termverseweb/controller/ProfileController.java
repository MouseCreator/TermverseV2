package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/profile")
@RequestPrefix("/profile")
@Controller
@RequiredArgsConstructor
public class ProfileController {
    @URL
    @Get
    public void create(@FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        System.out.println("\n\n" + optionalAuthentication.isPresent());
    }
}
