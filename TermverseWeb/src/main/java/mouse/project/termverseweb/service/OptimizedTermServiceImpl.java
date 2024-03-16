package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.progress.TermProgressPair;
import mouse.project.termverseweb.dto.progress.TermProgressUpdates;
import mouse.project.termverseweb.dto.term.TermWithProgressResponseDTO;
import mouse.project.termverseweb.exception.UpdateException;
import mouse.project.termverseweb.mapper.TermMapper;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserTerm;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.TermRepository;
import mouse.project.termverseweb.repository.UserTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OptimizedTermServiceImpl implements OptimizedTermService {

    private final StudySetRepository studySetRepository;
    private final UserTermRepository repository;
    private final TermRepository termRepository;
    private final TermMapper termMapper;
    @Autowired
    public OptimizedTermServiceImpl(StudySetRepository studySetRepository,
                                    UserTermRepository repository,
                                    TermMapper termMapper, TermRepository termRepository) {
        this.studySetRepository = studySetRepository;
        this.repository = repository;
        this.termRepository = termRepository;
        this.termMapper = termMapper;
    }

    @Override
    public List<TermWithProgressResponseDTO> updateAll(TermProgressUpdates updates) {
        List<TermProgressPair> updatesList = updates.getUpdatesList();
        Long userId = updates.getUserId();
        List<UserTerm> userTerms = createUserTerms(userId, updatesList);
        Map<Long, UserTerm> saved = new HashMap<>();
        userTerms.forEach(ut -> {
            UserTerm save = repository.save(ut);
            saved.put(ut.getTerm().getId(), save);
        });
        List<Long> termIds = updates.getUpdatesList().stream().map(TermProgressPair::getTerm).toList();
        List<Term> terms = termRepository.findAllByIds(termIds);

        return toTermsWithProgress(terms, saved, userId);
    }

    private List<TermWithProgressResponseDTO> toTermsWithProgress(List<Term> terms,
                                                                  Map<Long, UserTerm> saved,
                                                                  Long userId) {
        List<TermWithProgressResponseDTO> result = new ArrayList<>();
        for (Term term : terms) {
            UserTerm userTerm = saved.get(term.getId());
            if (userTerm == null) {
                throw new UpdateException("No progress defined for user " + userId + " and term " + term.getId());
            }
            String progress = userTerm.getProgress();
            TermWithProgressResponseDTO responseWithProgress = termMapper.toResponseWithProgress(term, progress);
            result.add(responseWithProgress);
        }
        return result;
    }

    private List<Term> getTermsFromSet(Long studySetId) {
        Optional<StudySet> byId = studySetRepository.findAllByIdWithTerms(studySetId);
        if(byId.isEmpty()) {
            throw new EntityNotFoundException("No study set with id " + studySetId + " found");
        }
        return byId.get().getTerms();
    }

    private List<UserTerm> createUserTerms(Long userId, List<TermProgressPair> updatesList) {
        return updatesList.stream().map(u -> {
            UserTerm userTerm = new UserTerm();
            userTerm.setProgress(u.getProgress());
            userTerm.setUser(new User(userId));
            userTerm.setTerm(new Term(u.getTerm()));
            return userTerm;
        }).toList();
    }

    @Override
    public List<TermWithProgressResponseDTO> getForUserFromStudySet(Long userId, Long studySetId) {
        List<Term> termsFromSet = getTermsFromSet(studySetId);
        List<Long> idList = termsFromSet.stream().map(Term::getId).toList();
        List<UserTerm> userTerm = repository.findByUserAndTerms(userId, idList);
        Map<Long, UserTerm> map = new HashMap<>();
        userTerm.forEach(ut -> map.put(ut.getTerm().getId(), ut));
        return toTermsWithProgress(termsFromSet, map, userId);
    }
}
