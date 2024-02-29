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

@RequiredArgsConstructor
@Service
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    private final GenericServiceHelper genericService;
    @Override
    public List<StudySetResponseDTO> findAll() {
        return genericService.with(repository).findAll().to(studySetMapper::toResponse);
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
        genericService.with(repository).removeById(id);
    }

    @Override
    public void restoreById(Long id) {
        genericService.use(repository).none(r -> r.restoreById(id));
    }

    @Override
    public StudySetResponseDTO save(StudySetCreateDTO model) {
        return genericService.with(repository).save(model, studySetMapper::fromCreate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO update(StudySetUpdateDTO model) {
        return genericService.with(repository).update(model, studySetMapper::fromUpdate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findById(Long id) {
        return genericService.with(repository).findById(id).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findByIdIncludeDeleted(Long id) {
        return genericService.use(repository).
                optional(r -> r.findByIdIncludeDeleted(id)).
                orThrow(studySetMapper::toResponse);
    }
}
