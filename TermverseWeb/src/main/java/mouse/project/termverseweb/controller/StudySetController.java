package mouse.project.termverseweb.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.resolver.CurrentUserContext;
import mouse.project.termverseweb.service.StudySetService;
import mouse.project.termverseweb.service.UserStudySetService;
import mouse.project.termverseweb.service.help.AuthService;
import mouse.project.termverseweb.service.optimized.OptimizedStudySetService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sets")
@RequestPrefix("/sets")
@Controller
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService service;
    private final OptimizedStudySetService optimized;
    private final AuthService auth;

    private final UserStudySetService userStudySetService;
    @URL
    @Get
    @GetMapping
    public List<StudySetWithOwnerDTO> findAll() {
        List<StudySetResponseDTO> all = service.findAll();
        return toOwnerStudySet(all);
    }

    @NotNull
    private List<StudySetWithOwnerDTO> toOwnerStudySet(List<StudySetResponseDTO> all) {
        return all.stream().map(s -> {
            UserResponseDTO owner = userStudySetService.getOwnerOfStudySet(s.getId());
            return new StudySetWithOwnerDTO(s, owner.getName());
        }).toList();
    }

    @URL("/byuser/[id]")
    @Get
    @GetMapping("/byuser/{id}")
    public List<StudySetWithOwnerDTO> findAllByUser(@PathVariable("id") @FromURL("id") Long id) {
        List<StudySetResponseDTO> studySetsByUser = service.findStudySetsByUser(id);
        return toOwnerStudySet(studySetsByUser);
    }

    @URL
    @Post
    @PostMapping
    public StudySetWithTermsResponseDTO create(
            @FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
            @RBody @RequestBody StudySetCreateDTO createDTO) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        return optimized.create(userId, createDTO);
    }

    @URL("/[id]")
    @Get
    @GetMapping("/{id}")
    public StudySetResponseDTO findById(@PathVariable("id") @FromURL("id") Long id) {
        return service.findById(id);
    }

    @URL("/full/[id]")
    @Get
    @GetMapping("/full/{id}")
    public StudySetWithTermsResponseDTO findFullById(@PathVariable("id") @FromURL("id") Long id) {
        return service.findByIdWithTerms(id);
    }
    @URL("/[id]")
    @Update
    @PutMapping("/{id}")
    public StudySetWithTermsResponseDTO update(@FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
                                               @RBody @RequestBody StudySetSubmitDTO dto) {
        if (optionalAuthentication.isEmpty()) {
            throw new StatusException(401);
        }
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        return optimized.update(userId, dto);
    }

    @URL("/[id]")
    @Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") @FromURL("id") Long id) {
        service.deleteById(id);
    }

    @URL("/role/[id]")
    @Get
    @GetMapping("/role/{id}")
    public UserStudySetResponseDTO role(@PathVariable("id") @FromURL("id") Long sid,
                                        @CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        try {
            return userStudySetService.getByUserAndStudySet(userId, sid);
        } catch (EntityNotFoundException e) {
            UserStudySetResponseDTO dto = new UserStudySetResponseDTO();
            dto.setStudySetId(sid);
            dto.setUserId(userId);
            dto.setType("none");
            return dto;
        }
    }
    @URL("/author/[id]")
    @Get
    @GetMapping("/author/{id}")
    public UserResponseDTO author(@PathVariable("id") @FromURL("id") Long sid) {
        return userStudySetService.getOwnerOfStudySet(sid);
    }

    @URL("/saved")
    @Get
    @GetMapping("/saved")
    public List<StudySetWithOwnerDTO> saved(@CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        List<StudySetDescriptionDTO> studySetsByUser = optimized.getStudySetsByUser(userId);
        return studySetsByUser.stream().map(s -> {
            UserResponseDTO owner = userStudySetService.getOwnerOfStudySet(s.getId());
            return new StudySetWithOwnerDTO(s, owner.getName());
        }).toList();
    }
}
