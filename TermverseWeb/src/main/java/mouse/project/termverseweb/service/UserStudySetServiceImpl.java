package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.UserStudySetMapper;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.UserRepository;
import mouse.project.termverseweb.repository.UserStudySetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStudySetServiceImpl implements UserStudySetService {
    private final UserStudySetRepository repository;
    private final StudySetRepository studySetRepository;
    private final UserRepository userRepository;
    private final ServiceProviderContainer services;
    private final UserStudySetMapper mapper;
    private final UserTermService userTermService;
    public UserStudySetServiceImpl(UserStudySetRepository repository,
                                   StudySetRepository studySetRepository,
                                   UserRepository userRepository,
                                   ServiceProviderContainer services,
                                   UserStudySetMapper mapper,
                                   UserTermService userTermService) {
        this.repository = repository;
        this.studySetRepository = studySetRepository;
        this.userRepository = userRepository;
        this.services = services;
        this.mapper = mapper;
        this.userTermService = userTermService;
    }

    @Override
    @Transactional
    public UserStudySetResponseDTO save(UserStudySetCreateDTO userStudySetCreateDTO) {
        Long userId = userStudySetCreateDTO.getUserId();
        Long studySetId = userStudySetCreateDTO.getStudySetId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() ->
                new EntityNotFoundException("User with id " + userId + " not found."));
        Optional<StudySet> setOptional = studySetRepository.findById(studySetId);
        StudySet set = setOptional.orElseThrow(() ->
                new EntityNotFoundException("Set with id " + studySetId + " not found."));
        UserStudySet model = new UserStudySet(user, set, userStudySetCreateDTO.getType());
        UserStudySetResponseDTO result = services.use(repository).
                single(r -> r.save(model))
                .to(mapper::toResponse);

        userTermService.initializeProgress(userId, studySetId);
        return result;
    }

    @Override
    public UserStudySetResponseDTO update(Long userId, Long studySetId, String type) {
        UserStudySetUpdateDTO updateDTO = new UserStudySetUpdateDTO();
        updateDTO.setUserId(userId);
        updateDTO.setStudySetId(studySetId);
        updateDTO.setType(type);
        return update(updateDTO);
    }

    @Override
    public UserStudySetResponseDTO update(UserStudySetUpdateDTO userStudySetUpdateDTO) {
        return services.use(repository).
                single(r -> r.save(mapper.fromUpdate(userStudySetUpdateDTO)))
                .to(mapper::toResponse);
    }

    @Override
    public UserStudySetResponseDTO getByUserAndStudySet(Long userId, Long studySetId) {
        return services.use(repository)
                .optional(r -> r.findByUserAndStudySet(userId, studySetId))
                .orThrow(mapper::toResponse);
    }

    @Override
    public void removeByUserAndStudySet(Long userId, Long studySetId) {
        services.use(repository).none(r -> r.deleteByUserAndStudySet(userId, studySetId));
        userTermService.removeProgress(userId, studySetId);
    }

    @Override
    public List<UserStudySetResponseDTO> getAll() {
        return services.use(repository).multi(UserStudySetRepository::findAll).to(mapper::toResponse);
    }

    @Override
    @Transactional
    public UserStudySetResponseDTO save(Long userId, Long studySetId, String type) {
        UserStudySetCreateDTO createDTO = new UserStudySetCreateDTO();
        createDTO.setUserId(userId);
        createDTO.setStudySetId(studySetId);
        createDTO.setType(type);
        return save(createDTO);
    }
}
