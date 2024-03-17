package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;

import java.util.List;

public interface UserStudySetService {
    UserStudySetResponseDTO save(UserStudySetCreateDTO userStudySetCreateDTO);
    UserStudySetResponseDTO update(Long userId, Long studySetId, String relation);
    UserStudySetResponseDTO update(UserStudySetUpdateDTO userStudySetCreateDTO);
    UserStudySetResponseDTO getByUserAndStudySet(Long userId, Long studySetId);
    void removeByUserAndStudySet(Long userId, Long studySetId);
    List<UserStudySetResponseDTO> getAll();
    UserStudySetResponseDTO save(Long userId, Long studySetId, String type);
}
