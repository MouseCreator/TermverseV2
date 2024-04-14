package mouse.project.lib.service.provider;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.service.GenericServiceHelper;
import mouse.project.lib.service.helper.CrudHelper;
import mouse.project.lib.service.model.IdIterable;
import mouse.project.lib.service.repository.CustomCrudRepository;
import mouse.project.lib.service.result.OptionalResult;
import mouse.project.lib.service.result.RawResult;
import mouse.project.lib.service.result.RawResultCollection;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class CrudServiceProviderImpl implements CrudServiceProvider {
    private final GenericServiceHelper genericService;
    @Auto
    public CrudServiceProviderImpl(GenericServiceHelper genericService) {
        this.genericService = genericService;
    }

    public <MODEL extends IdIterable<ID>, ID> CrudServiceState<MODEL, ID> with(CustomCrudRepository<MODEL, ID> repository) {
        return new CrudServiceState<>(repository, genericService);
    }

    public static class CrudServiceState<MODEL  extends IdIterable<ID>, ID> implements CrudHelper<MODEL, ID> {
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

        @Override
        public RawResult<MODEL> save(Supplier<MODEL> fromCreate) {
            return save(fromCreate.get());
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

        public <UPDATE> RawResult<MODEL> update(UPDATE updateDTO,
                                                Function<UPDATE, MODEL> fromUpdate) {
            return genericService.use(repository).single(r -> {
                MODEL model = fromUpdate.apply(updateDTO);
                Optional<MODEL> byId = r.findById(model.getId());
                byId.orElseThrow(EntityNotFoundException::new);
                return r.save(model);
            });
        }

        public RawResult<MODEL> update(MODEL model) {
            return genericService.use(repository).single(
                    r -> {
                        Optional<MODEL> byId = r.findById(model.getId());
                        byId.orElseThrow(EntityNotFoundException::new);
                        return r.save(model);
                    });
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
