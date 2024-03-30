package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.tag.TagUpdateDTO;
import mouse.project.termverseweb.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface TagMapper {
    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "userById")
    Tag fromCreate(TagCreateDTO createDTO);
    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "userById")
    Tag fromUpdate(TagUpdateDTO createDTO);
    @Mapping(source = "owner", target = "ownerId", qualifiedByName = "userToId")
    TagResponseDTO toResponse(Tag createDTO);
}
