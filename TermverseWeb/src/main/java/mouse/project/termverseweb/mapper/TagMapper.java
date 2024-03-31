package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.tag.TagUpdateDTO;
import mouse.project.termverseweb.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface TagMapper {
    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "userById")
    Tag fromCreate(TagCreateDTO createDTO);
    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "userById")
    Tag fromUpdate(TagUpdateDTO createDTO);
    @Mapping(source = "owner", target = "ownerId", qualifiedByName = "userToId")
    TagResponseDTO toResponse(Tag createDTO);

    @Named("tagById")
    default Tag getUserById(Long id) {
        return id == null ? null : new Tag(id);
    }
    @Named("tagToId")
    default Long getUserId(Tag user) {
        return user == null ? null : user.getId();
    }
}
