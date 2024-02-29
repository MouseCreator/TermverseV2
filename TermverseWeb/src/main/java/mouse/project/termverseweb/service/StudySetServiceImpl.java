package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.lib.service.helper.CrudServiceHelper;
import mouse.project.termverseweb.lib.service.GenericServiceHelper;
import mouse.project.termverseweb.lib.service.helper.SoftDeleteServiceHelper;
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
    private final CrudServiceHelper crud;
    private final SoftDeleteServiceHelper soft;
    private final GenericServiceHelper genericService;
    @Override
    public List<StudySetResponseDTO> findAll() {
        return crud.with(repository).findAll().to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllIncludeDeleted() {
        return soft.with(repository).findAllWithDeleted().to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByNameIgnoreCase(String name) {
        return genericService.use(repository).multi(r -> r.findAllByNameIgnoreCase(name)).to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return genericService.
                use(repository).
                multi(r -> r.findAllByCreatedDateRange(startDate, endDate)).
                to(studySetMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        crud.with(repository).removeById(id);
    }

    @Override
    public void restoreById(Long id) {
        soft.with(repository).restoreById(id);
    }

    @Override
    public StudySetResponseDTO save(StudySetCreateDTO model) {
        return crud.with(repository).save(model, studySetMapper::fromCreate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO update(StudySetUpdateDTO model) {
        return crud.with(repository).update(model, studySetMapper::fromUpdate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findById(Long id) {
        return crud.with(repository).findById(id).orThrow(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findByIdIncludeDeleted(Long id) {
        return soft.with(repository).getByIdIncludeDeleted(id).orThrow(studySetMapper::toResponse);
    }
}
