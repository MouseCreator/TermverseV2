package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.settag.SetTagCreateDTO;
import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetTagService {
    List<SetTagResponseDTO> getAll();
    SetTagResponseDTO save(SetTagCreateDTO setTag);
    SetTagResponseDTO save(Long userId, Long setId, Long tagId);
    SetTagResponseDTO getSetTagById(Long userId, Long setId, Long tagId);
    List<StudySetResponseDTO> getStudySetsByUserAndTags(Long userId, List<Long> tagIds);
    void delete(Long userId, Long setId, Long tagId);
}
