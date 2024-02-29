package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.*;
import mouse.project.termverseweb.model.StudySet;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface StudySetMapper extends ModelMapper<StudySet,
        StudySetCreateDTO,
        StudySetUpdateDTO,
        StudySetResponseDTO> {
}
