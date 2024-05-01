package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationHandler;
import mouse.project.termverseweb.service.UserStudySetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user_sets")
@RequestPrefix("/user_sets")
@Controller
public class UserStudySetController {

    private final OptionalAuthorizationHandler auth;
    private final UserStudySetService service;
    @Auto
    public UserStudySetController(OptionalAuthorizationHandler auth,
                                  UserStudySetService service) {
        this.auth = auth;
        this.service = service;
    }

    @URL("/[id]")
    @Post
    public UserStudySetResponseDTO create(@FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
                       @FromURL("id") Long id) {
        Long userId = auth.toUserId(optionalAuthentication);
        return service.save(new UserStudySetCreateDTO(userId, id, UserStudySetRelation.VIEWER));
    }
    @URL("/[id]")
    @Delete
    public void delete(@FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
                                          @FromURL("id") Long id) {
        Long userId = auth.toUserId(optionalAuthentication);
        service.remove(userId, id);
    }
}
