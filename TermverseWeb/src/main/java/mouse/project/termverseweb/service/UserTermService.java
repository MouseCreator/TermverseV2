package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.progress.TermProgressResponseDTO;
import mouse.project.termverseweb.dto.progress.TermProgressUpdateDTO;

import java.util.List;

public interface UserTermService {
    TermProgressResponseDTO update(TermProgressUpdateDTO dto);
    TermProgressResponseDTO getById(Long id);
    List<TermProgressResponseDTO> updateAll(List<TermProgressUpdateDTO> dtoList);
    List<TermProgressResponseDTO> getAll();
    List<TermProgressResponseDTO> getForTerms(Long userId, List<Long> termIds);
    void removeAll(Long userId, List<Long> termIds);
    List<TermProgressResponseDTO> getForStudySet(Long userId, Long studySetId);
    TermProgressResponseDTO save(Long userId, Long studySetId, String progress);
    List<TermProgressResponseDTO> initializeProgress(Long userId, Long studySetId);
    void removeProgress(Long userId, Long studySetId);
}
