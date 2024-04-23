package mouse.project.termverseweb.controller;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.*;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.service.StudySetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sets")
@RequestPrefix("/sets")
@Controller
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService service;
    @URL
    @Get
    public List<StudySetResponseDTO> findAll() {
        return service.findAll();
    }

    @URL
    @Post
    public StudySetResponseDTO create(@RBody StudySetCreateDTO createDTO) {
        return service.save(createDTO);
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
