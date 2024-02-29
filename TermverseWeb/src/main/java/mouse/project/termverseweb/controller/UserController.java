package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    @GetMapping
    public List<UserResponseDTO> findAll() {
        return userService.findAll();
    }
}
