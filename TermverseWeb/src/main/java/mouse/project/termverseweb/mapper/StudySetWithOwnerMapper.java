package mouse.project.termverseweb.mapper;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.User;
import org.springframework.stereotype.Component;

@Service
@Component
public class StudySetWithOwnerMapper {
    private final UserMapper userMapper;
    private final StudySetMapper studySetMapper;

    public StudySetWithOwnerMapper(UserMapper userMapper, StudySetMapper studySetMapper) {
        this.userMapper = userMapper;
        this.studySetMapper = studySetMapper;
    }

    public StudySetWithOwnerDTO toStudySetWithOwner(User user, StudySet studySet) {
        UserResponseDTO userResponseDTO = userMapper.toResponse(user);
        StudySetResponseDTO studySetResponseDTO = studySetMapper.toResponse(studySet);
        return new StudySetWithOwnerDTO(studySetResponseDTO, userResponseDTO);
    }
}
