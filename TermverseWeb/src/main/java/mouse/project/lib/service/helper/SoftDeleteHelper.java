package mouse.project.lib.service.helper;

import mouse.project.lib.service.result.OptionalResult;
import mouse.project.lib.service.result.RawResultCollection;

public interface SoftDeleteHelper<MODEL, ID> {
    OptionalResult<MODEL> getByIdIncludeDeleted(ID id);
    void restoreById(ID id);
    RawResultCollection<MODEL> findAllWithDeleted();
}
