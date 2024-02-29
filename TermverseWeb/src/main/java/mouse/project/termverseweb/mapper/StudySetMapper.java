package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;


import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.model.StudySet;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface StudySetMapper {
    StudySetResponseDTO toResponse(StudySet model);
    StudySet fromCreate(StudySetCreateDTO createDTO);
    StudySet fromUpdate(StudySetUpdateDTO updateDTO);
}
