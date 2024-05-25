package mouse.project.termverseweb.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.data.StudySetSearchParams;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sets")
@RequestPrefix("/sets")
@Controller
@RequiredArgsConstructor
@Log4j2
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
        return List.of();
    }

    @URL("/search")
    @Get
    @GetMapping("/search")
    public List<StudySetWithOwnerDTO> findBySearchParams(
            @Param("page") @RequestParam("page") int pageNumber,
            @Param("size") @RequestParam("size") int pageSize,
            @Param("q") @RequestParam("q") String searchParam,
            @Param("category") @RequestParam("category") String category,
            @Param("sort") @RequestParam("sort") String sortBy,
            @FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication userAuth
    ) {
        StudySetSearchParams searchParams = StudySetSearchParams.build()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .searchParam(searchParam)
                .category(category)
                .sort(sortBy)
                .user(auth.onAuth(userAuth).toUserId())
                .get();
        return service.findAllBySearchParams(searchParams);
    }

    @URL("/byuser/[id]")
    @Get
    @GetMapping("/byuser/{id}")
    public List<StudySetWithOwnerDTO> findAllByUser(@PathVariable("id") @FromURL("id") Long id) {
        List<StudySetResponseDTO> studySetsByUser = service.findStudySetsByUser(id);
        log.debug("Found all study sets by user with id: " + id);
        return List.of();
    }

    @URL
    @Post
    @PostMapping
    public StudySetWithTermsResponseDTO create(
            @FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
            @RBody @RequestBody StudySetCreateDTO createDTO) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        StudySetWithTermsResponseDTO studySet = optimized.create(userId, createDTO);
        log.debug("User " + userId + " created new study set. New study set id: " + studySet.getId());
        return studySet;
    }

    @URL("/[id]")
    @Get
    @GetMapping("/{id}")
    public StudySetResponseDTO findById(@PathVariable("id") @FromURL("id") Long id) {
        log.debug("Performing search study set by id: " + id);
        return service.findById(id);
    }

    @URL("/full/[id]")
    @Get
    @GetMapping("/full/{id}")
    public StudySetWithTermsResponseDTO findFullById(@PathVariable("id") @FromURL("id") Long id) {
        log.debug("Performing search study set by id: " + id);
        return service.findByIdWithTerms(id);
    }
    @URL("/[id]")
    @Update
    @PutMapping("/{id}")
    public StudySetWithTermsResponseDTO update(@FromAttribute(Args.OPT_AUTH) @CurrentUserContext OptionalAuthentication optionalAuthentication,
                                               @RBody @RequestBody StudySetSubmitDTO dto) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        log.debug("User " + userId + " updates study set id: " + dto.getId());
        return optimized.update(userId, dto);
    }

    @URL("/[id]")
    @Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") @FromURL("id") Long id) {
        log.debug("Deleting study set: " + id);
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
        log.debug("Getting author of study set: " + sid);
        return userStudySetService.getOwnerOfStudySet(sid);
    }

    @URL("/saved")
    @Get
    @GetMapping("/saved")
    public List<StudySetWithOwnerDTO> saved(@CurrentUserContext @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication) {
        Long userId = auth.onAuth(optionalAuthentication).toUserId();
        log.debug("Getting all study sets of user " + userId);
        List<StudySetDescriptionDTO> studySetsByUser = optimized.getStudySetsByUser(userId);
        return List.of();
    }
}
