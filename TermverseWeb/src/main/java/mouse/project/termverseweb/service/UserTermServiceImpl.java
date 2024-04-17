package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import mouse.project.termverseweb.repository.TermRepository;
import mouse.project.termverseweb.repository.UserRepository;
import mouse.project.termverseweb.repository.UserTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@mouse.project.lib.ioc.annotation.Service
public class UserTermServiceImpl implements UserTermService {
    private final StudySetRepository studySetRepository;
    private final UserTermRepository repository;
    private final TermProgressMapper mapper;
    private final ServiceProviderContainer services;
    private final TermRepository termRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserTermServiceImpl(StudySetRepository studySetRepository,
                               UserTermRepository repository,
                               TermProgressMapper mapper,
                               ServiceProviderContainer services,
                               TermRepository termRepository,
                               UserRepository userRepository) {
        this.studySetRepository = studySetRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.services = services;
        this.termRepository = termRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TermProgressResponseDTO update(TermProgressUpdateDTO dto) {
        return services.use(repository).single(r -> r.save(mapper.fromUpdate(dto))).to(mapper::toResponse);
    }

    @Override
    @Transactional
    public List<TermProgressResponseDTO> updateAll(List<TermProgressUpdateDTO> dtoList) {
        return dtoList.stream().map(dto ->
            services.use(repository)
                    .single(r -> r.save(mapper.fromUpdate(dto)))
                    .to(mapper::toResponse)
        ).toList();
    }

    @Override
    public List<TermProgressResponseDTO> getAll() {
        return services.use(repository).multi(UserTermRepository::findAll).to(mapper::toResponse);
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
    @Transactional
    public TermProgressResponseDTO save(Long userId, Long termId, String progress) {
        UserTerm userTerm = new UserTerm();
        User user = services.crud(userRepository)
                .findById(userId)
                .orThrow("No user with id: " + userId);
        userTerm.setUser(user);
        Term term = services.crud(termRepository)
                .findById(termId)
                .orThrow("No term with id: " + termId);
        userTerm.setTerm(term);
        userTerm.setProgress(progress);
        return services.use(repository).single(r -> r.save(userTerm)).to(mapper::toResponse);
    }

    @Override
    @Transactional
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
    @Transactional
    public void removeProgress(Long userId, Long studySetId) {
        List<Long> terms = getTermsIdsFromSet(studySetId);
        removeAll(userId, terms);
    }

    @Override
    public int countStudied(Long userId, Long studySetId) {
        List<Long> termsIdsFromSet = getTermsIdsFromSet(studySetId);
        return (int) repository.findByUserAndTerms(userId, termsIdsFromSet)
                .stream().filter(p -> Progress.FAMILIAR.equals(p.getProgress()))
                .count();
    }
}
