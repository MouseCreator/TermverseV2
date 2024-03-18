package mouse.project.termverseweb.service.optimized;

import mouse.project.termverseweb.dto.progress.TermProgressUpdates;
import mouse.project.termverseweb.dto.term.TermWithProgressResponseDTO;

import java.util.List;

public interface OptimizedTermService {
    List<TermWithProgressResponseDTO> updateAll(TermProgressUpdates updates);
    List<TermWithProgressResponseDTO> getForUserFromStudySet(Long userId, Long studySetId);
    List<TermWithProgressResponseDTO> initializeProgress(Long userId, Long studySetId);
    List<TermWithProgressResponseDTO> resetProgress(Long userId, Long studySetId);
    void removeProgress(Long userId, Long studySetId);

    int getUserProgress(Long userId, Long studySetId);
}
