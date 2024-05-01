package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.filters.argument.OptionalAuthorizationHandler;
import mouse.project.termverseweb.service.StudySetService;
import mouse.project.termverseweb.service.UserStudySetService;
import mouse.project.termverseweb.service.optimized.OptimizedStudySetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/sets")
@RequestPrefix("/sets")
@Controller
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService service;
    private final OptimizedStudySetService optimized;
    private final OptionalAuthorizationHandler auth;

    private final UserStudySetService userStudySetService;
    @URL
    @Get
    public List<StudySetResponseDTO> findAll() {
        return service.findAll();
    }

    @URL("/byuser/[id]")
    @Get
    public List<StudySetResponseDTO> findAllByUser(@FromURL("id") Long id) {
        return service.findStudySetsByUser(id);
    }

    @URL
    @Post
    public StudySetWithTermsResponseDTO create(
            @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
            @RBody StudySetCreateDTO createDTO) {
        Long userId = auth.toUserId(optionalAuthentication);
        return optimized.create(userId, createDTO);
    }

    @URL("/[id]")
    @Get
    public StudySetResponseDTO findById(@FromURL("id") Long id) {
        return service.findById(id);
    }

    @URL("/full/[id]")
    @Get
    public StudySetWithTermsResponseDTO findFullById(@FromURL("id") Long id) {
        return service.findByIdWithTerms(id);
    }
    @URL("/[id]")
    @Update
    public StudySetWithTermsResponseDTO update(@FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
                                      @RBody StudySetSubmitDTO dto) {
        if (optionalAuthentication.isEmpty()) {
            throw new StatusException(401);
        }
        Long userId = auth.toUserId(optionalAuthentication);
        return optimized.update(userId, dto);
    }

    @URL("/[id]")
    @Delete
    public void delete(@FromURL("id") Long id) {
        service.deleteById(id);
    }

    @URL("/role/[id]")
    @Get
    public UserStudySetResponseDTO role(@FromURL("id") Long sid, @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        Long userId = auth.toUserId(optionalAuthentication);
        return userStudySetService.getByUserAndStudySet(userId, sid);
    }
    @URL("/author/[id]")
    @Get
    public UserResponseDTO author(@FromURL("id") Long sid) {
        return userStudySetService.getOwnerOfStudySet(sid);
    }
}
