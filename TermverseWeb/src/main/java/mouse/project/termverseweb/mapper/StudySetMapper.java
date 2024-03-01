package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;


import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.model.StudySet;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface StudySetMapper {
    StudySetResponseDTO toResponse(StudySet model);
    StudySet fromCreate(StudySetCreateDTO createDTO);
    StudySet fromUpdate(StudySetUpdateDTO updateDTO);
    @Named("studySetById")
    default StudySet getStudySetById(Long id) {
        return id == null ? null : new StudySet(id);
    }
    @Named("studySetToId")
    default Long getStudySetId(StudySet studySet) {
        return studySet == null ? null : studySet.getId();
    }
}
