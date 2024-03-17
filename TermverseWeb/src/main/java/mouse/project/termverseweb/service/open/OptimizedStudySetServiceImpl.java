package mouse.project.termverseweb.service.open;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.studyset.*;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.user.UserWithRelation;
import mouse.project.termverseweb.exception.EntityStateException;
import mouse.project.termverseweb.exception.MissingEntityException;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.model.SizedStudySet;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.service.UserStudySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptimizedStudySetServiceImpl implements OptimizedStudySetService {

    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    private final ServiceProviderContainer services;
    private final UserStudySetService userStudySetService;
    @Autowired
    public OptimizedStudySetServiceImpl(StudySetRepository repository,
                                        StudySetMapper studySetMapper,
                                        ServiceProviderContainer services,
                                        UserStudySetService userStudySetService) {
        this.repository = repository;
        this.studySetMapper = studySetMapper;
        this.services = services;
        this.userStudySetService = userStudySetService;
    }

    @Override
    @Transactional
    public StudySetWithTermsResponseDTO create(StudySetWithCreatorDTO createDTO) {
        Long creatorId = createDTO.getCreatorId();
        StudySet saved = services.crud(repository).save(createDTO, studySetMapper::fromCreator).getRaw();
        userStudySetService.save(creatorId, saved.getId(), UserStudySetRelation.OWNER);
        return studySetMapper.toResponseWithTerms(saved);
    }

    @Override
    public StudySetDescriptionDTO getShortDescription(Long id) {
        Optional<SizedStudySet> byIdWithSize = repository.findByIdWithSize(id);
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
        UserWithRelation result = owners.get(0);
        relatedUsers.remove(result);
        return result.getResponseDTO();
    }


    @Override
    public List<StudySetDescriptionDTO> getStudySetsByUser(Long userId) {
        return null;
    }

    @Override
    public List<StudySetDescriptionDTO> getStudySetsByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudySet> allByUserIdAndPage = repository.findAllByUserIdAndPage(userId, pageable);
        List<StudySet> studySets = allByUserIdAndPage.stream().toList();
        return null;
    }

    @Override
    public StudySetDescriptionWithProgressDTO getDescription(Long id, Long userId) {
        return null;
    }

    @Override
    public StudySetHeaderResponseDTO getHeader(Long id) {
        return null;
    }
}
