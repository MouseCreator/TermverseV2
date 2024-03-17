package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class UserStudySetFactory implements Factory{
    public UserStudySetCreateDTO getCreateDTO(Long userId, Long studySetId, String type) {
        UserStudySetCreateDTO userStudySetCreateDTO = new UserStudySetCreateDTO();
        userStudySetCreateDTO.setUserId(userId);
        userStudySetCreateDTO.setStudySetId(studySetId);
        userStudySetCreateDTO.setType(type);
        return userStudySetCreateDTO;
    }
}
