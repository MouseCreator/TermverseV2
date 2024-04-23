package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
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
    public void crete(@FromAttribute("KC_DATABASE_ID") Long id) {
        System.out.println(id);
    }
}
