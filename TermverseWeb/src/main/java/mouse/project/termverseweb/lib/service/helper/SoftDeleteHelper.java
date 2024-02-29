package mouse.project.termverseweb.lib.service.helper;

import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;

public interface SoftDeleteHelper<MODEL, ID> {
    OptionalResult<MODEL> getByIdIncludeDeleted(ID id);
    void restoreById(ID id);
    RawResultCollection<MODEL> findAllWithDeleted();
}
