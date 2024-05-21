package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationHandler;
import mouse.project.termverseweb.mapper.UserMapper;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequestPrefix("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final OptionalAuthorizationHandler auth;
    private final UserMapper mapper;
    @Autowired
    @Auto
    public UserController(UserService userService, OptionalAuthorizationHandler auth, UserMapper mapper) {
        this.userService = userService;
        this.auth = auth;
        this.mapper = mapper;
    }

    @GetMapping
    @Get
    @URL
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/current")
    @Get
    @URL("/current")
    public UserResponseDTO userInfo(@FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        User user = auth.toUser(optionalAuthentication);
        return mapper.toResponse(user);
    }

    @GetMapping("/{id}")
    @Get
    @URL("/[id]")
    public UserResponseDTO findById(@FromURL("id") @PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @Post
    @URL
    public UserResponseDTO create(
            @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
            @RBody UserCreateDTO userCreateDTO) {

        UserResponseDTO saved = userService.save(userCreateDTO);
        auth.registerUser(optionalAuthentication, saved.getId());
        return saved;
    }
}
