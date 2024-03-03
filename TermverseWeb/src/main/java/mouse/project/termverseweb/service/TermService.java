package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermUpdateDTO;

import java.util.List;

public interface TermService {
    List<TermResponseDTO> getAll();
    TermResponseDTO getById(Long id);
    TermResponseDTO save(TermCreateDTO userStudySetCreateDTO);
    TermResponseDTO update(TermUpdateDTO userStudySetCreateDTO);
    void removeById(Long id);
    List<TermResponseDTO> getAllWithDeleted();
}
