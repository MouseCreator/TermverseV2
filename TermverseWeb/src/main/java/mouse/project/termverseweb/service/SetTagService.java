package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.model.SetTag;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetTagService {
    SetTagResponseDTO save(SetTag setTag);
    SetTagResponseDTO save(Long userId, Long setId, Long tagId);
    SetTagResponseDTO getSetTagById(Long userId, Long setId, Long tagId);
    List<StudySetResponseDTO> getStudySetsByUserAndTags(@Param("userId") Long userId, @Param("tagIds") List<Long> tagIds);
    void delete(Long userId, Long setId, Long tagId);
}
