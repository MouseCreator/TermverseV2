package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.settag.SetTagCreateDTO;
import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.dto.settag.SetTagUpdateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.SetTagMapper;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.model.SetTag;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Tag;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.repository.SetTagRepository;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.TagRepository;
import mouse.project.termverseweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SetTagServiceImpl implements SetTagService {

    private final SetTagRepository repository;
    public final SetTagMapper mapper;
    private final ServiceProviderContainer services;

    private final StudySetMapper studySetMapper;

    private final UserRepository userRepository;
    private final StudySetRepository studySetRepository;
    private final TagRepository tagRepository;

    private SetTagResponseDTO save(SetTag setTag) {
        return services.use(repository)
                .single(r -> r.save(setTag))
                .to(mapper::toResponse);
    }

    @Override
    public SetTagResponseDTO save(SetTagCreateDTO setTag) {
        return save(setTag.getUserId(), setTag.getStudySetId(), setTag.getTagId());
    }

    @Override
    public SetTagResponseDTO save(Long userId, Long setId, Long tagId) {
       return getAndSave(userId, setId, tagId);
    }

    @Override
    public SetTagResponseDTO update(SetTagUpdateDTO setTag) {
        return update(setTag.getUserId(), setTag.getStudySetId(), setTag.getTagId());
    }

    @Override
    public SetTagResponseDTO update(Long userId, Long setId, Long tagId) {
        Optional<SetTag> setTagById = repository.getSetTagById(userId, setId, tagId);
        if (setTagById.isEmpty()) {
            String format = String.format("No setTag found by given id: %d %d %d", userId, setId, tagId);
            throw new EntityNotFoundException(format);
        }
        return getAndSave(userId, setId, tagId);
    }

    private SetTagResponseDTO getAndSave(Long userId, Long setId, Long tagId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<StudySet> studySetOpt = studySetRepository.findById(setId);
        Optional<Tag> tagOpt = tagRepository.findById(tagId);
        User user = unpack(userOpt, "User", userId);
        StudySet studySet = unpack(studySetOpt, "Study Set", setId);
        Tag tag = unpack(tagOpt, "Tag", tagId);
        SetTag setTag = new SetTag(user, studySet, tag);

        return save(setTag);
    }

    private <T> T unpack(Optional<T> optional, String name, Long id) {
        return optional.orElseThrow(() -> new EntityNotFoundException(name + " is not found by id: " + id));
    }

    @Override
    public SetTagResponseDTO getSetTagById(Long userId, Long setId, Long tagId) {
        return services.use(repository)
                .optional(r -> r.getSetTagById(userId, setId, tagId))
                .orThrow(mapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> getStudySetsByUserAndTags(Long userId, List<Long> tagIds) {
        return services.use(repository)
                .multi(r -> r.getStudySetsByUserAndTags(userId, tagIds))
                .to(studySetMapper::toResponse);
    }

    @Override
    public void delete(Long userId, Long setId, Long tagId) {
        services.use(repository).none(r -> r.delete(userId, setId, tagId));
    }
}
