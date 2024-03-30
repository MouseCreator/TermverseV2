package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.tag.TagUpdateDTO;

import java.util.List;

public interface TagService {
    List<TagResponseDTO> getAll();
    TagResponseDTO getById(Long id);
    TagResponseDTO save(TagCreateDTO createDTO);
    TagResponseDTO update(TagUpdateDTO updateDTO);
    void removeById(Long id);
    TagResponseDTO hardGet(Long id);
    List<TagResponseDTO> getAllWithDeleted();
    List<TagResponseDTO> getAllByUser(Long userId);
    List<TagResponseDTO> getAllByUserAndName(Long userId, String name);
    void restoreById(Long id);
}
