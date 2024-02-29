package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.service.StudySetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sets")
public class StudySetController {
    private final StudySetService studySetService;
    @GetMapping
    public List<StudySetResponseDTO> findAll() {
        return studySetService.findAll();
    }
}
