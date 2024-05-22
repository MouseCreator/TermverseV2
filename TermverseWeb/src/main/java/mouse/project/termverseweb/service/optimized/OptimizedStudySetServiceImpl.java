package mouse.project.termverseweb.service.optimized;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageDescriptionImpl;
import mouse.project.lib.web.exception.StatusException;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.dto.term.TermSubmitDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserWithRelation;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.exception.EntityStateException;
import mouse.project.termverseweb.exception.MissingEntityException;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.mapper.TermMapper;
import mouse.project.termverseweb.model.SizedStudySet;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.service.SetTermService;
import mouse.project.termverseweb.service.TermService;
import mouse.project.termverseweb.service.UserStudySetService;
import mouse.project.termverseweb.utils.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@mouse.project.lib.ioc.annotation.Service
public class OptimizedStudySetServiceImpl implements OptimizedStudySetService {

    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    private final ServiceProviderContainer services;
    private final UserStudySetService userStudySetService;
    private final OptimizedTermService optimizedTermService;
    private final TermMapper termMapper;
    private final TermService termService;
    private final SetTermService setTermService;
    @Autowired
    public OptimizedStudySetServiceImpl(StudySetRepository repository,
                                        StudySetMapper studySetMapper,
                                        ServiceProviderContainer services,
                                        UserStudySetService userStudySetService,
                                        OptimizedTermService optimizedTermService,
                                        TermMapper termMapper,
                                        TermService termService, SetTermService setTermService) {
        this.repository = repository;
        this.studySetMapper = studySetMapper;
        this.services = services;
        this.userStudySetService = userStudySetService;
        this.optimizedTermService = optimizedTermService;
        this.termMapper = termMapper;
        this.termService = termService;
        this.setTermService = setTermService;
    }

    @Override
    @Transactional
    public StudySetWithTermsResponseDTO create(Long creatorId, StudySetCreateDTO createDTO) {
        StudySet studySet = studySetMapper.fromCreate(createDTO);
        LocalDateTime localDateTime = DateUtils.timeNowToSeconds();
        studySet.setCreatedAt(localDateTime);
        StudySet saved = services.crud(repository).save(studySet).getRaw();
        userStudySetService.save(creatorId, saved.getId(), UserStudySetRelation.OWNER);
        return studySetMapper.toResponseWithTerms(saved);
    }

    @Override
    public StudySetDescriptionDTO getShortDescription(Long id) {
        Optional<SizedStudySet> byIdWithSize = (repository.findByIdWithSize(id));
        if (byIdWithSize.isEmpty()) {
            throw new EntityNotFoundException("Study set with id " + id + " not found");
        }
        SizedStudySet sizedStudySet = byIdWithSize.get();
        StudySet studySet = sizedStudySet.studySet();
        int termCount = sizedStudySet.size();
        return studySetMapper.toShortDescription(studySet, termCount);
    }

    private UserResponseDTO excludeOwner(List<UserWithRelation> relatedUsers, Long setId) {
        List<UserWithRelation> owners = relatedUsers.stream()
                .filter(user -> user.getRelationType().equals(UserStudySetRelation.OWNER))
                .toList();
        if (owners.isEmpty()) {
            throw new MissingEntityException("Study Set " + setId + " has no owner.");
        }
        if (owners.size() > 1) {
            throw new EntityStateException("Study Set " + setId + " has " + owners.size() + " owners.");
        }
        UserWithRelation result = owners.getFirst();
        relatedUsers.remove(result);
        return result.getResponseDTO();
    }


    @Override
    public List<StudySetDescriptionDTO> getStudySetsByUser(Long userId) {
        List<StudySet> studySets = repository.findAllByUserId(userId);
        return setsToDescription(studySets);
    }

    @Override
    public List<StudySetDescriptionDTO> getStudySetsByUser(Long userId, Integer page, Integer size) {
        PageDescription pageDescription = new PageDescriptionImpl(page, size);
        Page<StudySet> allByUserIdAndPage = repository.findAllByUserId(userId, pageDescription);
        List<StudySet> studySets = allByUserIdAndPage.getElements();
        return setsToDescription(studySets);
    }

    @NotNull
    private List<StudySetDescriptionDTO> setsToDescription(List<StudySet> studySets) {
        List<Integer> sizes = studySets.stream().map(s -> repository.getTermCount(s.getId())).toList();
        List<StudySetDescriptionDTO> result = new ArrayList<>();
        for (int i = 0; i < sizes.size(); i++) {
            result.add(studySetMapper.toShortDescription(studySets.get(i), sizes.get(i)));
        }
        return result;
    }

    @Override
    @Transactional
    public StudySetDescriptionWithProgressDTO getDescription(Long id, Long userId) {
        Optional<SizedStudySet> byIdWithSize = (repository.findByIdWithSize(id));
        if (byIdWithSize.isEmpty()) {
            throw new EntityNotFoundException("Study set with id " + id + " not found");
        }
        SizedStudySet sizedStudySet = byIdWithSize.get();
        int studiedTerms = optimizedTermService.getUserProgress(id, userId);
        return studySetMapper.toProgressDescription(sizedStudySet.studySet(), sizedStudySet.size(), studiedTerms);
    }

    @Override
    public StudySetHeaderResponseDTO getHeader(Long id) {
        StudySet studySet = services.crud(repository)
                .findById(id)
                .orThrow("Study set with id " + id + " not found");
        List<UserWithRelation> users = userStudySetService.getUsersByStudySet(id);
        UserResponseDTO owner = excludeOwner(users, id);
        List<UserResponseDTO> savers = users.stream().map(UserWithRelation::getResponseDTO).toList();
        return studySetMapper.toHeader(studySet, owner, savers);
    }

    @Override
    @Transactional
    public StudySetWithTermsResponseDTO update(Long issuer, StudySetSubmitDTO dto) {
        Long setId = dto.getId();
        UserStudySetResponseDTO byUserAndStudySet = userStudySetService.getByUserAndStudySet(issuer, setId);
        String type = byUserAndStudySet.getType();
        if (type.equals(UserStudySetRelation.VIEWER)) {
            throw new StatusException(403);
        }
        List<TermSubmitDTO> terms = dto.getTerms();
        List<Term> prevTerms = optimizedTermService.getAllByStudySet(setId);
        compareAndUpdate(setId, prevTerms, terms);
        Optional<StudySet> set = repository.findById(setId);
        if (set.isEmpty()) {
            throw new StatusException(404);
        }
        StudySet studySet = set.get();
        studySet.setName(dto.getName());
        studySet.setPictureUrl(dto.getPictureUrl());
        repository.save(studySet);
        Optional<StudySet> fSet = repository.findAllByIdWithTerms(setId);
        if (fSet.isEmpty()) {
            throw new StatusException(404);
        }
        return studySetMapper.toResponseWithTerms(fSet.get());
    }
    private record TermData(String t, String d) {
    }
    private void compareAndUpdate(Long setId, List<Term> prevTerms, List<TermSubmitDTO> terms) {
        List<Term> needToAdd = new ArrayList<>();
        List<Term> newOnes = new ArrayList<>();
        List<Term> needToRemove = new ArrayList<>();

        Map<TermData, Term> prevMap = new HashMap<>();
        for (Term term : prevTerms) {
            prevMap.put(new TermData(term.getTerm(), term.getDefinition()), term);
        }

        Map<TermData, TermSubmitDTO> currentMap = new HashMap<>();
        for (TermSubmitDTO term : terms) {
            currentMap.put(new TermData(term.getTerm(), term.getDefinition()), term);
        }
        for (TermData termData : prevMap.keySet()) {
            if (currentMap.containsKey(termData))
                continue;
            Term term = prevMap.get(termData);
            needToRemove.add(term);
        }
        for (TermData termData : currentMap.keySet()) {
            Term term = termMapper.fromSubmit(currentMap.get(termData));
            Term termPrev = prevMap.get(termData);
            if (termPrev != null)
                term.setId(termPrev.getId());
            else
                newOnes.add(term);
            needToAdd.add(term);
        }
        needToRemove.forEach(t -> termService.removeById(t.getId()));
        needToRemove.forEach(t -> setTermService.delete(setId, t.getId()));
        needToAdd.forEach(termService::save);
        newOnes.forEach(t -> setTermService.save(setId, t.getId()));
    }

}
