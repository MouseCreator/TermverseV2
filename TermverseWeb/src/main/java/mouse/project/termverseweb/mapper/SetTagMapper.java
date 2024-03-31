package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.settag.SetTagCreateDTO;
import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.dto.settag.SetTagUpdateDTO;
import mouse.project.termverseweb.model.SetTag;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = {TermMapper.class, UserMapper.class, StudySetMapper.class})
public interface SetTagMapper {
    SetTagResponseDTO toResponse(SetTag setTag);
    SetTag fromCreate(SetTagCreateDTO createDTO);
    SetTag fromUpdate(SetTagUpdateDTO updateDTO);
}
