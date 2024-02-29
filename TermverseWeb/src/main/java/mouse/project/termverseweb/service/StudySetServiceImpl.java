package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.repository.StudySetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    @Override
    public List<StudySetResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public List<StudySetResponseDTO> findAllIncludeDeleted() {
        return null;
    }

    @Override
    public List<StudySetResponseDTO> findAllByNameIgnoreCase(String name) {
        return null;
    }

    @Override
    public List<StudySetResponseDTO> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public StudySetResponseDTO save(StudySetCreateDTO model) {
        return null;
    }

    @Override
    public StudySetResponseDTO update(StudySetUpdateDTO model) {
        return null;
    }

    @Override
    public Optional<StudySetResponseDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<StudySetResponseDTO> findByIdIncludeDeleted(Long id) {
        return Optional.empty();
    }
}
