package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;

import org.springframework.stereotype.Service;

@Service
public class StudySetFactory implements Factory {
    public StudySetCreateDTO studySetCreateDTO(String setName) {
        StudySetCreateDTO createDTO = new StudySetCreateDTO();
        createDTO.setName(setName);
        return createDTO;
    }
}
