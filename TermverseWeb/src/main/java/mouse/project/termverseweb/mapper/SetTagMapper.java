package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.model.SetTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {TagMapper.class, UserMapper.class, StudySetMapper.class})
public interface SetTagMapper {
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToId")
    @Mapping(source = "studySet", target = "studySetId", qualifiedByName = "studySetToId")
    @Mapping(source = "tag", target = "tagId", qualifiedByName = "tagToId")
    SetTagResponseDTO toResponse(SetTag setTag);
}
