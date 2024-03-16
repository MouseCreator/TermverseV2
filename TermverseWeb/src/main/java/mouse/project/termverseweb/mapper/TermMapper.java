package mouse.project.termverseweb.mapper;

import mouse.project.termverseweb.config.MapperConfig;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermUpdateDTO;
import mouse.project.termverseweb.model.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface TermMapper {
    TermResponseDTO toResponse(Term model);
    @Mapping(target = "progress", source = "progress")
    TermResponseDTO toResponseWithProgress(Term model, String progress);
    Term fromCreate(TermCreateDTO createDTO);
    Term fromUpdate(TermUpdateDTO updateDTO);
    @Named("termToResponse")
    default List<TermResponseDTO> toResponse(List<Term> termList) {
        return termList.stream().map(this::toResponse).toList();
    }
    @Named("termFromCreate")
    default List<Term> fromCreate(List<TermCreateDTO> createDTOList) {
        return createDTOList.stream().map(this::fromCreate).toList();
    }
    @Named("termFromUpdate")
    default List<Term> fromUpdate(List<TermUpdateDTO> updateDTOs) {
        return updateDTOs.stream().map(this::fromUpdate).toList();
    }
    @Named("termToId")
    default Long getTermId(Term term) {
        return term == null ? null : term.getId();
    }

    @Named("termFromId")
    default Term getTermId(Long id) {
        return id == null ? null : new Term(id);
    }

}
