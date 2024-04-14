package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;

import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import org.springframework.stereotype.Service;

@Service
@mouse.project.lib.ioc.annotation.Service
public class StudySetFactory implements Factory {
    public StudySetCreateDTO studySetCreateDTO(String setName) {
        StudySetCreateDTO createDTO = new StudySetCreateDTO();
        createDTO.setName(setName);
        return createDTO;
    }

    public StudySetUpdateDTO studySetUpdateDTO(Long id, String newName) {
        StudySetUpdateDTO updateDTO = new StudySetUpdateDTO();
        updateDTO.setId(id);
        updateDTO.setName(newName);
        return updateDTO;
    }
}
