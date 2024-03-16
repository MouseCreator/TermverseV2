package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.defines.Progress;
import mouse.project.termverseweb.dto.progress.TermProgressResponseDTO;
import mouse.project.termverseweb.dto.progress.TermProgressUpdateDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.TermProgressMapper;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserTerm;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.UserTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserTermServiceImpl implements UserTermService {
    private final StudySetRepository studySetRepository;
    private final UserTermRepository repository;
    private final TermProgressMapper mapper;
    private final ServiceProviderContainer services;
    @Autowired
    public UserTermServiceImpl(StudySetRepository studySetRepository,
                               UserTermRepository repository,
                               TermProgressMapper mapper,
                               ServiceProviderContainer services) {
        this.studySetRepository = studySetRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.services = services;
    }

    @Override
    public TermProgressResponseDTO update(TermProgressUpdateDTO dto) {
        return services.crud(repository).update(dto, mapper::fromUpdate).to(mapper::toResponse);
    }

    @Override
    public TermProgressResponseDTO getById(Long id) {
        return services.crud(repository).findById(id).orThrow(mapper::toResponse);
    }

    @Override
    public List<TermProgressResponseDTO> updateAll(List<TermProgressUpdateDTO> dtoList) {
        return dtoList.stream().map(dto ->
            services.crud(repository)
                    .update(dto, mapper::fromUpdate)
                    .to(mapper::toResponse)
        ).toList();
    }

    @Override
    public List<TermProgressResponseDTO> getAll() {
        return services.crud(repository).findAll().to(mapper::toResponse);
    }

    @Override
    public List<TermProgressResponseDTO> getForTerms(Long userId, List<Long> termIds) {
        return services.use(repository).multi(r -> r.findByUserAndTerms(userId, termIds)).to(mapper::toResponse);
    }

    @Override
    public void removeAll(Long userId, List<Long> termIds) {
        repository.deleteByUserAndTerms(userId, termIds);
    }

    @Override
    public List<TermProgressResponseDTO> getForStudySet(Long userId, Long studySetId) {
        List<Long> terms = getTermsIdsFromSet(studySetId);
        return services.use(repository).multi(r -> r.findByUserAndTerms(userId, terms)).to(mapper::toResponse);
    }

    @Override
    public TermProgressResponseDTO save(Long userId, Long termId, String progress) {
        UserTerm userTerm = new UserTerm();
        userTerm.setUser(new User(userId));
        userTerm.setTerm(new Term(termId));
        userTerm.setProgress(progress);
        return services.crud(repository).save(userTerm).to(mapper::toResponse);
    }

    @Override
    public List<TermProgressResponseDTO> initializeProgress(Long userId, Long studySetId) {
        List<Long> termIds = getTermsIdsFromSet(studySetId);
        List<TermProgressResponseDTO> responseDTOS = new ArrayList<>();
        termIds.forEach(t -> responseDTOS.add(save(userId, t, Progress.UNFAMILIAR)));
        return responseDTOS;
    }

    private List<Term> getTermsFromSet(Long studySetId) {
        Optional<StudySet> byId = studySetRepository.findAllByIdWithTerms(studySetId);
        if(byId.isEmpty()) {
            throw new EntityNotFoundException("No study set with id " + studySetId + " found");
        }
        return byId.get().getTerms();
    }
    private List<Long> getTermsIdsFromSet(Long studySetId) {
        return getTermsFromSet(studySetId).stream().map(Term::getId).toList();
    }

    @Override
    public void removeProgress(Long userId, Long studySetId) {
        List<Long> terms = getTermsIdsFromSet(studySetId);
        removeAll(userId, terms);
    }
}
