package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.progress.TermProgressUpdates;
import mouse.project.termverseweb.dto.term.TermWithProgressResponseDTO;

import java.util.List;

public interface OptimizedTermService {
    List<TermWithProgressResponseDTO> updateAll(TermProgressUpdates updates);
    List<TermWithProgressResponseDTO> getForUserFromStudySet(Long userId, Long studySetId);
}
