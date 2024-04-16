package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.Get;
import mouse.project.lib.web.annotation.RequestPrefix;
import mouse.project.lib.web.annotation.URL;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequestPrefix("/users")
@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    @Auto
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Get
    @URL
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }
}
