package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.UserStudySetMapper;
import mouse.project.termverseweb.repository.UserStudySetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserStudySetServiceImpl implements UserStudySetService {
    private final UserStudySetRepository repository;
    private final ServiceProviderContainer services;
    private final UserStudySetMapper mapper;

    public UserStudySetServiceImpl(UserStudySetRepository repository,
                                   ServiceProviderContainer services,
                                   UserStudySetMapper mapper) {
        this.repository = repository;
        this.services = services;
        this.mapper = mapper;
    }

    @Override
    public UserStudySetResponseDTO save(UserStudySetCreateDTO userStudySetCreateDTO) {
        return services.crud(repository).save(userStudySetCreateDTO, mapper::fromCreate).to(mapper::toResponse);
    }

    @Override
    public UserStudySetResponseDTO update(UserStudySetUpdateDTO userStudySetUpdateDTO) {
        return services.crud(repository).update(userStudySetUpdateDTO, mapper::fromUpdate).to(mapper::toResponse);
    }

    @Override
    public UserStudySetResponseDTO getById(Long id) {
        return services.crud(repository).findById(id).orThrow(mapper::toResponse);
    }

    @Override
    public void removeById(Long id) {
        services.crud(repository).removeById(id);
    }

    @Override
    public List<UserStudySetResponseDTO> getAll() {
        return services.crud(repository).findAll().to(mapper::toResponse);
    }

    @Override
    public UserStudySetResponseDTO save(Long userId, Long studySetId, String type) {
        UserStudySetCreateDTO createDTO = new UserStudySetCreateDTO();
        createDTO.setUserId(userId);
        createDTO.setStudySetId(studySetId);
        createDTO.setType(type);
        return save(createDTO);
    }
}
