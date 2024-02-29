package mouse.project.termverseweb.lib.service.helper;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.lib.service.GenericServiceHelper;
import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.result.RawResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CrudServiceHelper implements CrudServiceProvider {
    private final GenericServiceHelper genericService;
    @Autowired
    public CrudServiceHelper(GenericServiceHelper genericService) {
        this.genericService = genericService;
    }

    public <MODEL, ID> CrudServiceState<MODEL, ID> with(CustomCrudRepository<MODEL, ID> repository) {
        return new CrudServiceState<>(repository, genericService);
    }

    public static class CrudServiceState<MODEL, ID> implements CrudHelper<MODEL, ID> {
        private final CustomCrudRepository<MODEL, ID> repository;
        private final GenericServiceHelper genericService;
        public CrudServiceState(CustomCrudRepository<MODEL, ID> repository, GenericServiceHelper genericService) {
            this.repository = repository;
            this.genericService = genericService;
        }

        public <CREATE> RawResult<MODEL> save(CREATE createDTO,
                                              Function<CREATE, MODEL> fromCreate) {
            return genericService.use(repository).single(r -> r.save(fromCreate.apply(createDTO)));
        }

        public RawResult<MODEL> save(MODEL model) {
            return genericService.use(repository).single(r -> r.save(model));
        }

        public RawResultCollection<MODEL> findAll() {
            return genericService.use(repository).multi(CustomCrudRepository::findAll);
        }

        public OptionalResult<MODEL> findById(ID id) {
            return genericService.use(repository).optional(r -> r.findById(id));
        }

        public <UPDATE> RawResult<MODEL> update(UPDATE createDTO,
                                                Function<UPDATE, MODEL> fromCreate) {
            return genericService.use(repository).single(r -> r.save(fromCreate.apply(createDTO)));
        }

        public RawResult<MODEL> update(MODEL model) {
            return genericService.use(repository).single(r -> r.save(model));
        }

        public void safeRemove(ID id) {
            genericService.use(repository).none(
                    r -> {
                        r.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot remove model by id " + id));
                        r.deleteById(id);
                    });
        }

        public void removeById(ID id) {
            genericService.use(repository).none(r -> r.deleteById(id));
        }
    }

}
