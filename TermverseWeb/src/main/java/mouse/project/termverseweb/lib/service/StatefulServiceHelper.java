package mouse.project.termverseweb.lib.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.lib.service.result.RawResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class StatefulServiceHelper<MODEL, ID> {
    private final CustomCrudRepository<MODEL, ID> repository;
    public StatefulServiceHelper(CustomCrudRepository<MODEL, ID> repository) {
        this.repository = repository;
    }
    public <CREATE> RawResult<MODEL> save(CREATE createDTO,
                                          Function<CREATE, MODEL> fromCreate) {
        MODEL model = fromCreate.apply(createDTO);
        MODEL savedModel = repository.save(model);
        return RawResult.from(savedModel);
    }

    public RawResult<MODEL> save(MODEL model) {
        MODEL savedModel = repository.save(model);
        return RawResult.from(savedModel);
    }
    public RawResultCollection<MODEL> findAll() {
        List<MODEL> models = repository.findAll();
        return RawResultCollection.from(models);
    }
    public RawResult<MODEL> findById(ID id) {
        Optional<MODEL> modelOptional = repository.findById(id);
        MODEL model = modelOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
        return RawResult.from(model);
    }
    public <UPDATE> RawResult<MODEL> update(UPDATE createDTO,
                                            Function<UPDATE, MODEL> fromCreate) {
        MODEL model = fromCreate.apply(createDTO);
        MODEL savedModel = repository.save(model);
        return RawResult.from(savedModel);
    }
    public RawResult<MODEL> update(MODEL model) {
        MODEL savedModel = repository.save(model);
        return RawResult.from(savedModel);
    }
    public void removeById(ID id) {
        repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
        repository.deleteById(id);
    }

}
