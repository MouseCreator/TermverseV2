package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.security.resolver.CurrentUserContext;
import mouse.project.termverseweb.service.UserStudySetService;
import mouse.project.termverseweb.service.help.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user_sets")
@RequestPrefix("/user_sets")
@Controller
public class UserStudySetController {

    private final AuthService auth;
    private final UserStudySetService service;
    @Auto
    @Autowired
    public UserStudySetController(AuthService auth,
                                  UserStudySetService service) {
        this.auth = auth;
        this.service = service;
    }

    @URL("/[id]")
    @Post
    @PostMapping("/{id}")
    public UserStudySetResponseDTO create(@FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
                       @FromURL("id") @PathVariable("id") Long id) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        return service.save(new UserStudySetCreateDTO(userId, id, UserStudySetRelation.VIEWER));
    }
    @URL("/[id]")
    @Delete
    @DeleteMapping("/{id}")
    public void delete(@FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
                                          @FromURL("id") @PathVariable("id") Long id) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        service.remove(userId, id);
    }
}
