package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;

import java.util.List;

public interface UserStudySetService {
    UserStudySetResponseDTO save(UserStudySetCreateDTO userStudySetCreateDTO);
    UserStudySetResponseDTO update(UserStudySetUpdateDTO userStudySetCreateDTO);
    UserStudySetResponseDTO getById(Long id);
    void removeById(Long id);
    List<UserStudySetResponseDTO> getAll();
    UserStudySetResponseDTO save(Long userId, Long studySetId, String type);
}
