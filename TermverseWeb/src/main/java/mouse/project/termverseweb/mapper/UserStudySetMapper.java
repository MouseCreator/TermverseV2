package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;
import mouse.project.termverseweb.model.UserStudySet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {StudySetMapper.class, UserMapper.class})
public interface UserStudySetMapper {
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToId")
    @Mapping(source = "studySet", target = "studySetId", qualifiedByName = "studySetToId")
    UserStudySetResponseDTO toResponse(UserStudySet userStudySet);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userById")
    @Mapping(source = "studySetId", target = "studySet", qualifiedByName = "studySetById")
    UserStudySet fromCreate(UserStudySetCreateDTO userStudySet);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userById")
    @Mapping(source = "studySetId", target = "studySet", qualifiedByName = "studySetById")
    UserStudySet fromUpdate(UserStudySetUpdateDTO userStudySet);
}
