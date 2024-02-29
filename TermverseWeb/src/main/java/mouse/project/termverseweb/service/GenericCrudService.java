package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.genetic.CreateDTO;
import mouse.project.termverseweb.dto.genetic.ResponseDTO;
import mouse.project.termverseweb.dto.genetic.UpdateDTO;
import mouse.project.termverseweb.mapper.Mapper;
import mouse.project.termverseweb.mapper.ModelMapper;

import mouse.project.termverseweb.repository.CustomCrudRepository;

import java.util.List;
import java.util.Optional;

public class GenericCrudService<MODEL, CREATE extends CreateDTO<MODEL>,
        UPDATE extends UpdateDTO<MODEL>, RESPONSE extends ResponseDTO<MODEL>, ID> {
    private final CustomCrudRepository<MODEL, ID> repository;
    private final ModelMapper<MODEL, CREATE, UPDATE, RESPONSE> mapper;
    public GenericCrudService(CustomCrudRepository<MODEL, ID> repository,
                              ModelMapper<MODEL, CREATE, UPDATE, RESPONSE> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseDTO<MODEL> save(CREATE createDTO) {
        MODEL model = mapper.fromCreate(createDTO);
        MODEL savedModel = repository.save(model);
        return mapper.toResponse(savedModel);
    }
    public List<ResponseDTO<MODEL>> findAll() {
        List<MODEL> models = repository.findAll();
        return toResponse(models);
    }

    public RESPONSE getById(ID id) {
        Optional<MODEL> modelOptional = repository.findById(id);
        MODEL model = modelOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
        return Mapper.transform(model, mapper::toResponse);
    }

    public RESPONSE update(UPDATE updateDTO) {
        MODEL model = mapper.fromUpdate(updateDTO);
        MODEL savedModel = repository.save(model);
        return Mapper.transform(savedModel, mapper::toResponse);
    }
    public void removeById(ID id) {
        repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
        repository.deleteById(id);
    }

    private List<ResponseDTO<MODEL>> toResponse(List<MODEL> models) {
        return Mapper.mapAll(models).toAndGet(mapper::toResponse);
    }
}
