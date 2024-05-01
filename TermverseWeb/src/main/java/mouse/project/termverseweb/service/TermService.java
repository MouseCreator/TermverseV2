package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermUpdateDTO;
import mouse.project.termverseweb.model.Term;

import java.util.List;

public interface TermService {
    List<TermResponseDTO> getAll();
    TermResponseDTO getById(Long id);
    TermResponseDTO save(TermCreateDTO userStudySetCreateDTO);
    Term save(Term term);
    TermResponseDTO update(TermUpdateDTO userStudySetCreateDTO);
    void removeById(Long id);
    List<TermResponseDTO> getAllWithDeleted();
    void restoreById(Long term);
    List<TermResponseDTO> getByStudySet(Long setId);
}
