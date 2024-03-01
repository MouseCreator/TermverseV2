package mouse.project.termverseweb.service;


import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;


import java.time.LocalDateTime;
import java.util.List;

public interface StudySetService {
    List<StudySetResponseDTO> findAll();
    List<StudySetResponseDTO> findAllIncludeDeleted();
    List<StudySetResponseDTO> findAllByNameIgnoreCase(String name);
    List<StudySetResponseDTO> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate);
    void deleteById(Long id);
    void restoreById(Long id);
    StudySetResponseDTO save(StudySetCreateDTO model);
    StudySetResponseDTO update(StudySetUpdateDTO model);
    StudySetResponseDTO findById(Long id);
    StudySetResponseDTO findByIdIncludeDeleted(Long id);
    List<StudySetResponseDTO> findStudySetsByUser(Long userId);
}
