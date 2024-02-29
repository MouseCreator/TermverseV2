package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;

import java.util.List;

public interface StudySetService {
    List<StudySetResponseDTO> findAll();
}
