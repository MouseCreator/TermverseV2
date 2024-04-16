package mouse.project.termverseweb.service;

import mouse.project.termverseweb.model.SetTerm;

import java.util.List;
import java.util.Optional;

public interface SetTermService {
    SetTerm save(Long setId, Long termId);
    void delete(Long setId, Long termId);
    Optional<SetTerm> get(Long setId, Long termId);
    int getTermCount(Long setId);
    List<SetTerm> getAll();
}
