package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;


import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetWithTermsResponseDTO;
import mouse.project.termverseweb.model.StudySet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = TermMapper.class)
public interface StudySetMapper {
    StudySetResponseDTO toResponse(StudySet model);

    @Mapping(source = "terms", target = "terms", qualifiedByName = "termFromCreate")
    StudySet fromCreate(StudySetCreateDTO createDTO);
    @Mapping(source = "terms", target = "terms", qualifiedByName = "termFromUpdate")
    StudySet fromUpdate(StudySetUpdateDTO updateDTO);
    @Named("studySetById")
    default StudySet getStudySetById(Long id) {
        return id == null ? null : new StudySet(id);
    }
    @Named("studySetToId")
    default Long getStudySetId(StudySet studySet) {
        return studySet == null ? null : studySet.getId();
    }
    @Mapping(source = "terms", target = "terms", qualifiedByName = "termToResponse")
    StudySetWithTermsResponseDTO toResponseWithTerms(StudySet studySet);
}
