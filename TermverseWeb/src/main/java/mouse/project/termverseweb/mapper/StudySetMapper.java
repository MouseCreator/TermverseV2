package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;


import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.model.StudySet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = TermMapper.class)
public interface StudySetMapper {
    StudySetResponseDTO toResponse(StudySet model);
    //@Mapping(source = "terms", target = "terms", qualifiedByName = "termFromCreate")
    StudySet fromCreate(StudySetCreateDTO createDTO);
    //@Mapping(source = "terms", target = "terms", qualifiedByName = "termFromUpdate")
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
    @Mapping(source = "terms", target = "terms", qualifiedByName = "termFromCreate")
    StudySet fromCreator(StudySetWithCreatorDTO studySetWithCreatorDTO);
    @Mapping(target = "size", source = "termCount")
    StudySetDescriptionDTO toShortDescription(StudySet studySet, int termCount);
    @Mapping(target = "size", source = "size")
    @Mapping(target = "termsLearned", source = "studiedTerms")
    StudySetDescriptionWithProgressDTO toProgressDescription(StudySet studySet, int size, int studiedTerms);
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "savedByUsers", source = "savers")
    @Mapping(target = "id", source = "studySet.id")
    @Mapping(target = "name", source = "studySet.name")
    StudySetHeaderResponseDTO toHeader(StudySet studySet, UserResponseDTO owner, List<UserResponseDTO> savers);
}
