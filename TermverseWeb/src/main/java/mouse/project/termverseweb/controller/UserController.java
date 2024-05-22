package mouse.project.termverseweb.controller;

import lombok.extern.log4j.Log4j2;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.mapper.UserMapper;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.resolver.CurrentUserContext;
import mouse.project.termverseweb.service.UserService;
import mouse.project.termverseweb.service.help.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequestPrefix("/users")
@Controller
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final AuthService authService;
    @Autowired
    @Auto
    public UserController(UserService userService, UserMapper mapper, AuthService authService) {
        this.userService = userService;
        this.mapper = mapper;
        this.authService = authService;
    }

    @GetMapping
    @Get
    @URL
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @Get
    @URL("/current")
    @GetMapping("/current")
    public UserResponseDTO userInfo(@CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        return doUserInfo(optionalAuthentication);
    }
    private UserResponseDTO doUserInfo(Object authParam) {
        User user = authService.onAuth(authParam).toUser();
        return mapper.toResponse(user);
    }

    @GetMapping("/{id}")
    @Get
    @URL("/[id]")
    public UserResponseDTO findById(@FromURL("id") @PathVariable("id") Long id) {
        return userService.getById(id);
    }
}
