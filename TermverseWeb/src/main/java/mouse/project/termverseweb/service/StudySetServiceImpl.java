package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.repository.StudySetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    @Override
    public List<StudySetResponseDTO> findAll() {
        return List.of();
    }
}
