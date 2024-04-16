package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.StudySetTermRepository;
import mouse.project.termverseweb.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
@Service
@org.springframework.stereotype.Service
public class SetTermServiceImpl implements SetTermService {

    private final StudySetTermRepository repository;

    private final ServiceProviderContainer services;

    private final StudySetRepository studySetRepository;

    private final TermRepository termRepository;
    @Autowired
    @Auto
    public SetTermServiceImpl(StudySetTermRepository repository,
                              ServiceProviderContainer services,
                              StudySetRepository studySetRepository,
                              TermRepository termRepository) {
        this.repository = repository;
        this.services = services;
        this.studySetRepository = studySetRepository;
        this.termRepository = termRepository;
    }

    @Override
    @Transactional
    public SetTerm save(Long setId, Long termId) {
        Optional<StudySet> setOptional = studySetRepository.findById(setId);
        if (setOptional.isEmpty()) {
            throw new EntityNotFoundException("No set with id: " + setId);
        }
        Optional<Term> termOptional = termRepository.findById(termId);
        if (termOptional.isEmpty()) {
            throw new EntityNotFoundException("No term with id: " + termId);
        }
        SetTerm setTerm = new SetTerm(setOptional.get(), termOptional.get());
        return services.use(repository).single(r -> r.save(setTerm)).getRaw();
    }

    @Override
    public void delete(Long setId, Long termId) {
        services.use(repository).none(r -> r.delete(termId, setId));
    }

    @Override
    public Optional<SetTerm> get(Long setId, Long termId) {
        return services.use(repository).optional(r -> r.findById(termId, setId)).getRaw();
    }

    @Override
    public int getTermCount(Long setId) {
        return repository.getTermCount(setId);
    }

    @Override
    public List<SetTerm> getAll() {
        return repository.findAll();
    }
}
