package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.filters.argument.Args;
import mouse.project.termverseweb.filters.argument.OptionalAuthentication;
import mouse.project.termverseweb.service.StudySetService;
import mouse.project.termverseweb.service.optimized.OptimizedStudySetService;
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
    @URL
    @Get
    public List<StudySetResponseDTO> findAll() {
        return service.findAll();
    }

    @URL
    @Post
    public StudySetWithTermsResponseDTO create(
            @FromAttribute(Args.OPT_AUTH) OptionalAuthentication optionalAuthentication,
            @RBody StudySetCreateDTO createDTO) {
        if (optionalAuthentication.isEmpty()) {
            throw new StatusException(401);
        }
        return optimized.create(optionalAuthentication.getUserDatabaseId(), createDTO);
    }

    @URL("/[id]")
    @Get
    public StudySetResponseDTO findById(@FromURL("id") Long id) {
        return service.findById(id);
    }

    @URL("/[id]")
    @Update
    public StudySetResponseDTO update(@RBody StudySetUpdateDTO dto) {
        return service.update(dto);
    }

    @URL("/[id]")
    @Delete
    public void delete(@FromURL("id") Long id) {
        service.deleteById(id);
    }
}
