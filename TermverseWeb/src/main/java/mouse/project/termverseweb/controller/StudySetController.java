package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.service.StudySetService;
import mouse.project.termverseweb.service.optimized.OptimizedStudySetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/sets")
@RequestPrefix("/sets")
@Controller
@RequiredArgsConstructor
public class StudySetController {
    private final OptimizedStudySetService optimized;
    private final StudySetService service;
    @GetMapping
    @URL
    @Get
    public List<StudySetResponseDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    @URL
    @Post
    public StudySetWithTermsResponseDTO crete(@RBody StudySetWithCreatorDTO createDTO) {
        return optimized.create(createDTO);
    }

    @GetMapping("/header")
    @URL("/header/[id]")
    @Get()
    public StudySetHeaderResponseDTO header(@FromURL("id") Long id) {
        return optimized.getHeader(id);
    }
}
