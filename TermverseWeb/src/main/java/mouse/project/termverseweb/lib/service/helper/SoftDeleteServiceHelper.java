package mouse.project.termverseweb.lib.service.helper;

import mouse.project.termverseweb.lib.service.GenericServiceHelper;
import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SoftDeleteServiceHelper {
    private final GenericServiceHelper genericService;
    @Autowired
    public SoftDeleteServiceHelper(GenericServiceHelper genericService) {
        this.genericService = genericService;
    }

    public <MODEL, ID> SoftDeleteServiceState<MODEL, ID> with(SoftDeleteRepository<MODEL, ID> repository) {
        return new SoftDeleteServiceState<>(repository, genericService);
    }

    public static class SoftDeleteServiceState<MODEL, ID> {
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
