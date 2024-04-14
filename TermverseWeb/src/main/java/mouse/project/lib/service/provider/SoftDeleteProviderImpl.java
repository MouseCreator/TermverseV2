package mouse.project.lib.service.provider;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.service.GenericServiceHelper;
import mouse.project.lib.service.helper.SoftDeleteHelper;
import mouse.project.lib.service.repository.SoftDeleteRepository;
import mouse.project.lib.service.result.OptionalResult;
import mouse.project.lib.service.result.RawResultCollection;


@Service
public class SoftDeleteProviderImpl implements SoftDeleteProvider {
    private final GenericServiceHelper genericService;
    @Auto
    public SoftDeleteProviderImpl(GenericServiceHelper genericService) {
        this.genericService = genericService;
    }

    public <MODEL, ID> SoftDeleteServiceState<MODEL, ID> with(SoftDeleteRepository<MODEL, ID> repository) {
        return new SoftDeleteServiceState<>(repository, genericService);
    }

    public static class SoftDeleteServiceState<MODEL, ID> implements SoftDeleteHelper<MODEL, ID> {
        private final SoftDeleteRepository<MODEL, ID> repository;
        private final GenericServiceHelper genericService;

        public SoftDeleteServiceState(SoftDeleteRepository<MODEL, ID> repository, GenericServiceHelper genericService) {
            this.repository = repository;
            this.genericService = genericService;
        }

        public OptionalResult<MODEL> getByIdIncludeDeleted(ID id) {
            return genericService.use(repository).optional(r -> r.findByIdIncludeDeleted(id));
        }
        public void restoreById(ID id) {
            genericService.use(repository).none(r -> r.restoreById(id));
        }
        public RawResultCollection<MODEL> findAllWithDeleted() {
            return genericService.use(repository).multi(SoftDeleteRepository::findAllIncludeDeleted);
        }
    }
}
