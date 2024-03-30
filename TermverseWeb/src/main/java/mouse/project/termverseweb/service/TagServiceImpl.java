package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.tag.TagUpdateDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.TagMapper;
import mouse.project.termverseweb.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository repository;
    private final TagMapper tagMapper;
    private final ServiceProviderContainer services;
    @Override
    public List<TagResponseDTO> getAll() {
        return services.crud(repository).
                findAll().
                to(tagMapper::toResponse);
    }

    @Override
    public TagResponseDTO getById(Long id) {
        return services.crud(repository)
                .findById(id).orThrow(tagMapper::toResponse);
    }

    @Override
    public TagResponseDTO save(TagCreateDTO createDTO) {
        return services.
                crud(repository).
                save(createDTO, tagMapper::fromCreate).
                to(tagMapper::toResponse);
    }

    @Override
    public TagResponseDTO update(TagUpdateDTO updateDTO) {
        return services.
                crud(repository).
                update(updateDTO, tagMapper::fromUpdate).
                to(tagMapper::toResponse);
    }

    @Override
    public void removeById(Long id) {
        services.crud(repository).removeById(id);
    }

    @Override
    public TagResponseDTO hardGet(Long id) {
        return services.soft(repository)
                .getByIdIncludeDeleted(id).orThrow(tagMapper::toResponse);
    }

    @Override
    public List<TagResponseDTO> getAllWithDeleted() {
        return services.soft(repository)
                .findAllWithDeleted()
                .to(tagMapper::toResponse);
    }

    @Override
    public List<TagResponseDTO> getAllByUser(Long userId) {
        return services.use(repository).
                multi(r -> r.getTagsByOwner(userId)).
                to(tagMapper::toResponse);
    }

    @Override
    public List<TagResponseDTO> getAllByUserAndName(Long userId, String name) {
        return services.use(repository)
                .multi(r -> r.getTagsByOwnerAndName(userId, name))
                .to(tagMapper::toResponse);
    }

    @Override
    public void restoreById(Long id) {
        services.soft(repository).restoreById(id);
    }
}
