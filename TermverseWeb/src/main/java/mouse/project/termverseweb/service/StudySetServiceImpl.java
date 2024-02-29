package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;

import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
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
    private final ServiceProviderContainer services;
    @Override
    public List<StudySetResponseDTO> findAll() {
        return services.crud(repository).findAll().to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllIncludeDeleted() {
        return services.soft(repository).findAllWithDeleted().to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByNameIgnoreCase(String name) {
        return services.use(repository).multi(r -> r.findAllByNameIgnoreCase(name)).to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return services.
                use(repository).
                multi(r -> r.findAllByCreatedDateRange(startDate, endDate)).
                to(studySetMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        services.crud(repository).removeById(id);
    }

    @Override
    public void restoreById(Long id) {
        services.soft(repository).restoreById(id);
    }

    @Override
    public StudySetResponseDTO save(StudySetCreateDTO model) {
        return services.crud(repository).save(model, studySetMapper::fromCreate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO update(StudySetUpdateDTO model) {
        return services.crud(repository).update(model, studySetMapper::fromUpdate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findById(Long id) {
        return services.crud(repository).findById(id).orThrow(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findByIdIncludeDeleted(Long id) {
        return services.soft(repository).getByIdIncludeDeleted(id).orThrow(studySetMapper::toResponse);
    }
}
